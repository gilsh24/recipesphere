package com.example.recipesphere.ui.addrecipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesphere.model.User
import com.example.recipesphere.model.Model
import com.google.firebase.auth.FirebaseAuth

class AddRecipeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is AddRecipe Fragment"
    }

    val text: LiveData<String> = _text

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun getUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            Model.shared.getUser(uid) { result ->
                if (result.isSuccess) {
                    _user.value = result.getOrNull()
                } else {
                    _user.value = null
                }
            }
        } else {
            _user.value = null
        }
    }
}