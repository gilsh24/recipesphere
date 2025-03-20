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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipesphere.databinding.FragmentAddRecipeBinding
import com.example.recipesphere.model.Model
import com.example.recipesphere.model.Recipe
import com.example.recipesphere.ui.myrecipes.MyRecipesViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddRecipeViewModel by viewModels()
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var didSetProfileImage = false

    companion object {
        private const val ARG_RECIPE = "arg_recipe"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding.btnSubmit.setOnClickListener(::onSaveClicked)
    }

    private fun onSaveClicked(view: View?) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        viewModel.user.observe(viewLifecycleOwner){ currUser ->
            if (currUser != null){
                val userName = "${currUser.firstName} ${currUser.lastName}"
                val ingredients = binding.etIngredients.text.toString().split(",").map { it.trim() }
                Model.shared.getNutritionData(ingredients){ edamamResponse ->
                    if (edamamResponse != null){
                        val recipe = Recipe(
                            id = Random.nextInt(1, 1001).toString(),
                            title = binding.etRecipeName.text.toString(),
                            ingredients = binding.etIngredients.text.toString().split(",").map { it.trim() },
                            instructions = binding.etInstructions.text.toString(),
                            time = binding.etPreparationTime.text.toString(),
                            difficultyLevel = binding.etDifficultyLevel.text.toString().toIntOrNull() ?: 1,
                            userId = currUser.uid,
                            userName = userName ,
                            userAge = currUser.age,
                            calories = edamamResponse.calories,
                            dietLabels = edamamResponse.dietLabels,
                            healthLabels = edamamResponse.healthLabels,
                            cautions = edamamResponse.cautions,
                            mealType = edamamResponse.mealType,
                            cuisineType = edamamResponse.cuisineType
                        )
                        Model.shared.addRecipe(recipe, if (didSetProfileImage) (binding.RecipeImage.drawable as BitmapDrawable).bitmap else null) {
                            Toast.makeText(requireContext(), "Recipe added!", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                }

            } else{
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