package com.example.recipesphere.ui.general.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesphere.databinding.RecipeListItemBinding
import com.example.recipesphere.model.Recipe

class RecipesRecyclerAdapter(
    private var recipes: List<Recipe>?,
    private val isMyRecipe: Boolean) :
    RecyclerView.Adapter<RecipeViewHolder>() {

    var listener: OnItemClickListener? = null

    fun update(recipes: List<Recipe>?) {
        this.recipes = recipes;
    }

    override fun getItemCount(): Int = recipes?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding,isMyRecipe, listener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes?.get(position))
    }

}