package com.example.recipesphere.ui.browserecipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.recipesphere.model.Model
import com.example.recipesphere.model.Recipe

class BrowseRecipesViewModel : ViewModel() {
    var recipes: LiveData<List<Recipe>> = Model.shared.recipes

    fun refreshAllRecipes() {
        Model.shared.refreshAllRecipes()
    }
}