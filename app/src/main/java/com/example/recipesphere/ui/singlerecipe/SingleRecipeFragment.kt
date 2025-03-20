package com.example.recipesphere.ui.singlerecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recipesphere.databinding.FragmentSingleRecipeBinding
import com.example.recipesphere.model.Recipe
import com.squareup.picasso.Picasso

class SingleRecipeFragment : Fragment() {

    private var _binding: FragmentSingleRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SingleRecipeViewModel
    private var recipe: Recipe? = null

    companion object {
        private const val ARG_RECIPE = "arg_recipe"

        fun newInstance(recipe: Recipe): SingleRecipeFragment {
            val fragment = SingleRecipeFragment()
            val args = Bundle().apply {
                putParcelable(ARG_RECIPE, recipe)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SingleRecipeViewModel::class.java)
        recipe = arguments?.getParcelable<Recipe>(ARG_RECIPE)
        viewModel.recipe = recipe
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.recipe?.let { recipe ->
            // Bind data to views
            if (recipe.photoURL.isNotEmpty()){
                Picasso.get().load(recipe.photoURL).into(binding.ivRecipeImage)
            } else {
                binding.ivRecipeImage.setImageResource(com.example.recipesphere.R.drawable.recipe_avatar)
            }
            binding.tvUserDetails.text = "${recipe.userName}, ${recipe.userAge}"
            binding.tvRecipeTitle.text = recipe.title
            binding.tvTime.text = recipe.time
            binding.tvLikes.text = String.format(recipe.likes.toString())
            binding.tvDifficulty.text = "Difficulty ${recipe.difficultyLevel}/5"
            // Placeholder fields (to be updated later)
            binding.tvCalories.text = String.format(recipe.calories.toString())
            binding.tvDietLabel.text = recipe.dietLabels.toString()
            binding.tvHealthLabel.text = recipe.healthLabels.toString()
            binding.tvCuisineType.text = recipe.cuisineType.toString()
            binding.tvMealType.text = recipe.mealType.toString()
            binding.tvIngredients.text = recipe.ingredients.joinToString(", ")
            binding.tvInstructions.text = recipe.instructions
        } ?: run {
            // Handle case where recipe is null
            binding.tvRecipeTitle.text = "No recipe available"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}