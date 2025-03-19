package com.example.recipesphere.ui.singlerecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recipesphere.databinding.FragmentSingleRecipeBinding
import com.example.recipesphere.model.Recipe

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
            binding.ivRecipeImage.setImageResource(recipe.imageResId)
            binding.tvUserDetails.text = "${recipe.userName}, ${recipe.userAge}"
            binding.tvRecipeTitle.text = recipe.title
            binding.tvTime.text = recipe.time
            binding.tvLikes.text = recipe.likes.toString()
            binding.tvDifficulty.text = "Difficulty ${recipe.difficultyLevel}/5"
            // Placeholder fields (to be updated later)
            binding.tvCalories.text = "later"
            binding.tvDietLabel.text = "later"
            binding.tvHealthLabel.text = "later"
            binding.tvTotalNutrition.text = "later"
            binding.tvIngredients.text = recipe.ingredients.joinToString(", ")
            binding.tvInstructions.text = "Measure out the flour, salt and sugar if you’re using it and add to medium sized mixing bowl. Use a whisk or a wooden spoon to combine the dry ingredients together and set to the side. In a smaller bowl add the wet ingredients. The egg, milk, melted butter and vanilla extract. Give this a mix with a fork until well combined."
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