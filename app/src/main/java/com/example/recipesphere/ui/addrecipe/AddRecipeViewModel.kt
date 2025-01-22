package com.example.recipesphere.ui.addrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddRecipeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is AddRecipe Fragment"
    }
    val text: LiveData<String> = _text
}