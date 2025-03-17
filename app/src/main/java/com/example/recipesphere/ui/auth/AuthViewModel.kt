package com.example.recipesphere.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesphere.model.FirebaseModel
import com.example.recipesphere.model.User

class AuthViewModel : ViewModel() {
    private val firebaseModel = FirebaseModel()

    private val _registerResult = MutableLiveData<Result<Unit>>()
    val registerResult: LiveData<Result<Unit>> get() = _registerResult

    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> get() = _loginResult

    private val _userData = MutableLiveData<Result<User>>()
    val userData: LiveData<Result<User>> get() = _userData

    private val _updateResult = MutableLiveData<Result<Unit>>()
    val updateResult: LiveData<Result<Unit>> get() = _updateResult

    private val _userSignedIn = MutableLiveData<Boolean>()
    val userSignedIn: LiveData<Boolean> get() = _userSignedIn


    init{
        _userSignedIn.value = firebaseModel.isUserSignedIn()
    }
    fun registerUser(email: String, password: String, firstName: String, lastName: String, age: Int) {
        firebaseModel.registerUser(email, password, firstName, lastName, age) { result ->
            _registerResult.postValue(result)
        }
    }

    fun loginUser(email: String, password: String) {
        firebaseModel.signInUser(email, password) { result ->
            _loginResult.postValue(result)
        }
    }

    fun fetchUser(uid: String) {
        firebaseModel.getUser(uid) { result ->
            _userData.postValue(result)
        }
    }

    fun updateUser(uid: String, updates: Map<String, Any>) {
        firebaseModel.updateUser(uid, updates) { result ->
            _updateResult.postValue(result)
        }
    }

    fun signOut() {
        firebaseModel.signOut()
    }
}
