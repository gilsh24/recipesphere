package com.example.recipesphere.ui.general.recipeslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesphere.R
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

        binding.editbtn.setOnClickListener {
            listener?.onEditClick(recipe)
        }

        // Delete button click listener
        binding.deletebtn.setOnClickListener {
            listener?.onDeleteClick(recipe)
        }
    }

    fun bind(recipe: Recipe?) {
        recipe?.let {
            this.recipe = recipe
            binding.tvNameAge.text = "${recipe.userName}, ${recipe.userAge}"
            binding.tvRecipeTitle.text = "Recipe: ${recipe.title}"
            val ingredientsText = "Ingredients: ${recipe.ingredients.joinToString(", ")}"
            if (ingredientsText.length > 25) {
                binding.tvIngredients.text = ingredientsText.substring(0, 25) + "..."
            } else {
                binding.tvIngredients.text = ingredientsText
            }
            binding.tvTime.text = "${recipe.time} Minutes"
            if (recipe.photoURL.isNotEmpty()) {
                Picasso.get()
                    .load(recipe.photoURL)
                    .placeholder(R.drawable.recipe_avatar)
                    .into(binding.imgRecipe)
            } else {
                binding.imgRecipe.setImageResource(R.drawable.recipe_avatar)
            }

            binding.editbtn.visibility = if (isMyRecipe) View.VISIBLE else View.GONE
            binding.deletebtn.visibility = if (isMyRecipe) View.VISIBLE else View.GONE
        }
    }
}
