package com.example.recipesphere.ui.addrecipe

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipesphere.databinding.FragmentAddRecipeBinding
import com.example.recipesphere.model.Model
import com.example.recipesphere.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlin.random.Random

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddRecipeViewModel by viewModels()
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var didSetProfileImage = false
    private var recipeToEdit: Recipe? = null

    companion object {
        private const val ARG_RECIPE = "arg_recipe"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check if a recipe is passed for editing
        recipeToEdit = arguments?.getParcelable<Recipe>(ARG_RECIPE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            binding.RecipeImage.setImageBitmap(bitmap)
            didSetProfileImage = true
        }

        binding.takePhotoButton.setOnClickListener {
            cameraLauncher?.launch(null)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // If editing, pre-fill the form with the existing recipe data
        recipeToEdit?.let { recipe ->
            binding.etRecipeName.setText(recipe.title)
            binding.etIngredients.setText(recipe.ingredients.joinToString(", "))
            binding.etInstructions.setText(recipe.instructions)
            binding.etPreparationTime.setText(recipe.time)
            binding.etDifficultyLevel.setText(recipe.difficultyLevel.toString())
            if (recipe.photoURL.isNotEmpty()) {
                Picasso.get()
                    .load(recipe.photoURL)
                    .into(binding.RecipeImage)
            }
            binding.btnSubmit.text = "Update Recipe"
            didSetProfileImage = false // Reset to false; only set to true if a new image is taken
        } ?: run {
            binding.btnSubmit.text = "Add Recipe"
        }

        binding.btnSubmit.setOnClickListener(::onSaveClicked)
    }

    private fun onSaveClicked(view: View?) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        viewModel.user.observe(viewLifecycleOwner) { currUser ->
            if (currUser != null) {
                val userName = "${currUser.firstName} ${currUser.lastName}"
                val ingredients = binding.etIngredients.text.toString().split(",").map { it.trim() }
                Model.shared.getNutritionData(ingredients) { edamamResponse ->
                    if (edamamResponse != null) {
                        val recipe = if (recipeToEdit != null) {
                            recipeToEdit!!.copy(
                                id= recipeToEdit!!.id,
                                title = binding.etRecipeName.text.toString(),
                                ingredients = ingredients,
                                instructions = binding.etInstructions.text.toString(),
                                time = binding.etPreparationTime.text.toString(),
                                difficultyLevel = binding.etDifficultyLevel.text.toString().toIntOrNull() ?: 1,
                                userName = userName,
                                userAge = currUser.age,
                                calories = edamamResponse.calories,
                                dietLabels = edamamResponse.dietLabels,
                                healthLabels = edamamResponse.healthLabels,
                                cautions = edamamResponse.cautions,
                                mealType = edamamResponse.mealType,
                                cuisineType = edamamResponse.cuisineType
                            )
                        } else {
                            // Create new recipe
                            Recipe(
                                id = Random.nextInt(1, 1001).toString(), // Firebase will generate the ID
                                title = binding.etRecipeName.text.toString(),
                                ingredients = ingredients,
                                instructions = binding.etInstructions.text.toString(),
                                time = binding.etPreparationTime.text.toString(),
                                difficultyLevel = binding.etDifficultyLevel.text.toString().toIntOrNull() ?: 1,
                                userId = currUser.uid,
                                userName = userName,
                                userAge = currUser.age,
                                calories = edamamResponse.calories,
                                dietLabels = edamamResponse.dietLabels,
                                healthLabels = edamamResponse.healthLabels,
                                cautions = edamamResponse.cautions,
                                mealType = edamamResponse.mealType,
                                cuisineType = edamamResponse.cuisineType
                            )
                        }

                        val imageBitmap = if (didSetProfileImage) {
                            (binding.RecipeImage.drawable as BitmapDrawable).bitmap
                        } else {
                            null
                        }

                        if (recipeToEdit != null) {
                            // Update recipe
                            Model.shared.updateRecipe(recipe, imageBitmap) {
                                Toast.makeText(requireContext(), "Recipe updated!", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }
                        } else {
                            // Add new recipe
                            Model.shared.addRecipe(recipe, imageBitmap) {
                                Toast.makeText(requireContext(), "Recipe added!", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch nutrition data", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}