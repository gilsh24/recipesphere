package com.example.recipesphere.ui.general.recipeslist

import androidx.recyclerview.widget.RecyclerView
import com.example.recipesphere.databinding.RecipeListItemBinding
import com.example.recipesphere.model.Recipe
import com.squareup.picasso.Picasso

class RecipeViewHolder(
    private val binding: RecipeListItemBinding,
    private val isMyRecipe: Boolean,
    listener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

    private var recipe : Recipe? = null

    init {
        itemView.setOnClickListener {
            listener?.onItemClick(recipe)
        }
    }

    fun bind(recipe: Recipe?) {
        recipe?.let {
            this.recipe = recipe
            binding.tvNameAge.text = "${recipe.userName}, ${recipe.userAge}"
            binding.tvRecipeTitle.text = "Title: ${recipe.title}"
            binding.tvIngredients.text = "Ingredients: ${recipe.ingredients.joinToString(", ")}"
            binding.tvTime.text = recipe.time
            if (recipe.photoURL.isNotEmpty()) {
                Picasso.get()
                    .load(recipe.photoURL)
                    .into(binding.imgRecipe)
            } else {
                binding.imgRecipe.setImageResource(com.example.recipesphere.R.drawable.recipe_avatar)
            }
        }
    }
}
