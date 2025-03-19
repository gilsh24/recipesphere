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

    fun updateUserProfile(firstName: String, lastName: String, age: String, bitmap: Bitmap?, callback: (Result<Unit>) -> Unit) {
        _isLoading.value = true
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null && currentUser != null) {
            val updates = mutableMapOf<String, Any>(
                "firstName" to firstName,
                "lastName" to lastName,
                "age" to age.toInt()
            )

            bitmap?.let {
                model.uploadUserImage(it, currentUser!!) { result ->
                    if (result.isSuccess) {
                        model.updateUser(uid, updates + mapOf("photoURL" to result.getOrNull().toString())) { updateResult ->
                            _updateResult.value = updateResult
                            if(updateResult.isSuccess){
                                getUser()
                            }
                            _isLoading.value = false
                            callback(updateResult)
                        }
                    } else {
                        _updateResult.value = Result.failure(Exception("Image upload failed"))
                        _isLoading.value = false
                        callback(Result.failure(Exception("Image upload failed")))
                    }
                }
            } ?: run {
                model.updateUser(uid, updates) { updateResult ->
                    _updateResult.value = updateResult
                    if(updateResult.isSuccess){
                        getUser()
                    }
                    _isLoading.value = false
                    callback(updateResult)
                }
            }
        } else {
            _updateResult.value = Result.failure(Exception("User not logged in."))
            _isLoading.value = false
            callback(Result.failure(Exception("User not logged in.")))
        }
    }

}