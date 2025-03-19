package com.example.recipesphere.ui.general.recipeslist

import androidx.recyclerview.widget.RecyclerView
import com.example.recipesphere.databinding.RecipeListItemBinding
import com.example.recipesphere.model.Recipe

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
//            binding.imgRecipe.setImageResource(it.imageResId)
            binding.tvNameAge.text = "${recipe.userName}, ${recipe.userAge}"
            binding.tvRecipeTitle.text = recipe.title
            binding.tvDescription.text = "bla"
            binding.tvIngredients.text = "Ingredients: ${recipe.ingredients.joinToString(", ")}"
            binding.tvTime.text = recipe.time
            binding.tvLikes.text = recipe.likes.toString()
        }
    }
}
