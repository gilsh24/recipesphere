package com.example.recipesphere.ui.general.recipeslist

import androidx.recyclerview.widget.RecyclerView
import com.example.recipesphere.databinding.RecipeListItemBinding
import com.example.recipesphere.model.Recipe

class RecipeViewHolder(private val binding: RecipeListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe?) {
        recipe?.let {
            println("binding recipe: $it")
            binding.imgRecipe.setImageResource(it.imageResId)
            binding.tvNameAge.text = "${recipe.userName}, ${recipe.userAge}"
            binding.tvRecipeTitle.text = recipe.title
            binding.tvDescription.text = recipe.description
            binding.tvIngredients.text = "Ingredients: ${recipe.ingredients.joinToString(", ")}"
            binding.tvTime.text = recipe.time
            binding.tvLikes.text = recipe.likes.toString()
        }
    }
}
