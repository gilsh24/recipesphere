package com.example.recipesphere.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesphere.model.FirebaseModel

class RegistrationViewModel : ViewModel() {

    private val firebaseModel = FirebaseModel()

    private val _registrationResult = MutableLiveData<Result<Unit>>()
    val registrationResult: LiveData<Result<Unit>> = _registrationResult

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        age: Int
    ) {
        firebaseModel.registerUser(email, password, firstName, lastName, age) { result ->
            _registrationResult.value = result
        }
    }
}