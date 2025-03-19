package com.example.recipesphere.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesphere.model.Model

class AuthViewModel : ViewModel() {
    private val model = Model()

    private val _registerResult = MutableLiveData<Result<Unit>>()
    val registerResult: LiveData<Result<Unit>> get() = _registerResult

    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> get() = _loginResult

    private val _userSignedIn = MutableLiveData<Boolean>()
    val userSignedIn: LiveData<Boolean> get() = _userSignedIn

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init{
        _userSignedIn.value = model.isUserSignedIn()
    }

    fun registerUser(email: String, password: String, firstName: String, lastName: String, age: Int) {
        _isLoading.value = true
        model.registerUser(email, password, firstName, lastName, age) { result ->
            _registerResult.postValue(result)
            _isLoading.value = false
        }
    }

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        model.signInUser(email, password) { result ->
            _loginResult.postValue(result)
            _isLoading.value = false
        }
    }

}
