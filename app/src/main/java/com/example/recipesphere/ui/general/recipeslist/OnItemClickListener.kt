package com.example.recipesphere.ui.general.recipeslist

import com.example.recipesphere.model.Recipe

interface OnItemClickListener {
    fun onItemClick(recipe: Recipe?)
}