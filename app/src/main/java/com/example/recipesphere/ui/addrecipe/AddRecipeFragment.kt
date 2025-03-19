package com.example.recipesphere.ui.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private val viewModel: MyRecipesViewModel by viewModels()
    private var recipeToEdit: Recipe? = null

    companion object {
        private const val ARG_RECIPE = "arg_recipe"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeToEdit = arguments?.getParcelable<Recipe>(ARG_RECIPE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)

        recipeToEdit?.let { recipe ->
            binding.etRecipeName.setText(recipe.title)
            binding.etIngredients.setText(recipe.ingredients.joinToString(", "))
            binding.etInstructions.setText(recipe.instructions)
            binding.etPreparationTime.setText(recipe.time)
            binding.etDifficultyLevel.setText(recipe.difficultyLevel.toString())
            binding.btnSubmit.text = "Update Recipe"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener(::onSaveClicked)
    }

    private fun onSaveClicked(view: View?) {
        val title = binding.etRecipeName.text.toString()
        val ingredients = binding.etIngredients.text.toString().split(",").map { it.trim() }
        val instructions = binding.etInstructions.text.toString()
        val time = binding.etPreparationTime.text.toString()
        val difficultyLevel = binding.etDifficultyLevel.text.toString().toIntOrNull() ?: 1

        val recipe = if (recipeToEdit != null) {
            recipeToEdit!!.copy(
                title = title,
                ingredients = ingredients,
                instructions = instructions,
                time = time,
                difficultyLevel = difficultyLevel
            )
        } else {
            Recipe(
                id = Random.nextInt(1,1001).toString(),
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: "0",
                userName = "Amit",
                userAge = 23,
                title = title,
                difficultyLevel = difficultyLevel,
                ingredients = ingredients,
                instructions = instructions,
                time = time,
                likes = 0,
                imageResId = 123
            )
        }

        Model.shared.insertRecipe(recipe, null) {
            Toast.makeText(requireContext(), "Recipe added!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}