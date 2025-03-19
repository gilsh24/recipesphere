package com.example.recipesphere.ui.profile

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesphere.model.Model
import com.example.recipesphere.model.User
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel : ViewModel() {

    private val model = Model.shared

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _updateResult = MutableLiveData<Result<Unit>?>()
    val updateResult: LiveData<Result<Unit>?> get() = _updateResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var currentUser: User? = null

    fun getUser() {
        _isLoading.value = true
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            model.getUser(uid) { result ->
                if (result.isSuccess) {
                    Log.d("ProfileViewModel", "user: ${result.getOrNull()}")
                    _user.value = result.getOrNull()
                    currentUser = result.getOrNull()
                } else {
                    _user.value = null
                }
                _isLoading.value = false
            }
        } else {
            _user.value = null
            _isLoading.value = false
        }
    }
}