package com.example.recipesphere.model

import android.graphics.Bitmap
import android.os.Looper
import androidx.core.os.HandlerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesphere.base.EmptyCallback
import com.example.recipesphere.base.RecipeCallback
import com.example.recipesphere.model.dao.AppLocalDb
import com.example.recipesphere.model.dao.AppLocalDbRepository
import com.google.android.gms.auth.api.signin.internal.Storage
import java.util.concurrent.Executors

typealias EmptyCallback = () -> Unit

class Model {

    enum class LoadingState {
        LOADING,
        LOADED
    }

    private val database: AppLocalDbRepository = AppLocalDb.database
    private val executer = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel = FirebaseModel()
    private val cloudinaryModel = CloudinaryModel()
    val recipes: LiveData<List<Recipe>> = database.recipeDao().getAllRecipes()
    val loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()

    companion object {
        val shared = Model()
    }

    fun insertRecipe(recipe: Recipe, image: Bitmap?, storage: Storage?, callback: EmptyCallback) {
        firebaseModel.insertRecipe(recipe) {
            callback()
            // let this code stay here for later
//            image?.let {
//                uploadTo(
//                    storage,
//                    image = image,
//                    name = student.id,
//                    callback = { uri ->
//                        if (!uri.isNullOrBlank()) {
//                            val st = student.copy(avatarUrl = uri)
//                            firebaseModel.add(st, callback)
//                        } else {
//                            callback()
//                        }
//                    },
//                )
//            } ?: callback()
        }
    }

    // remember to change get by user id not by recipeId
    fun getUserRecipes(id: String, callback: RecipeCallback) {
        executer.execute {
            val recipe = database.recipeDao().getRecipeById(id)

            Thread.sleep(3000)

            mainHandler.post {
                callback(listOf(recipe))
            }
        }
    }
    fun registerUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        age: Int,
        callback: (Result<Unit>) -> Unit
    ) {
        firebaseModel.registerUser(email, password, firstName, lastName, age, callback)
    }

    fun signInUser(email: String, password: String, callback: (Result<Unit>) -> Unit) {
        firebaseModel.signInUser(email, password, callback)
    }

    fun getUser(uid: String, callback: (Result<User>) -> Unit) {
        firebaseModel.getUser(uid, callback)
    }

    fun isUserSignedIn(): Boolean {
        return firebaseModel.isUserSignedIn()
    }

    fun updateUser(uid: String, updates: Map<String, Any>, callback: (Result<Unit>) -> Unit) {
        firebaseModel.updateUser(uid, updates, callback)
    }

    fun update(user: User, image: Bitmap?, callback: EmptyCallback) {
        firebaseModel.updateUser(user.uid, user.toMap()) { result ->
            if (result.isSuccess) {
                image?.let {
                    cloudinaryModel.uploadImage(it, user.email, onSuccess = { uri ->
                        if (!uri.isNullOrBlank()) {
                            val updatedUser = user.copy(photoURL = uri)
                            firebaseModel.updateUser(updatedUser.uid, updatedUser.toMap()) { updateResult ->
                                if (updateResult.isSuccess) {
                                    callback()
                                } else {
                                    callback()
                                }
                            }
                        } else {
                            callback()
                        }
                    }, onError = { callback() })
                } ?: callback()
            } else {
                callback()
            }
        }
    }

    fun signOut() {
        firebaseModel.signOut()
    }


    fun uploadUserImage(
        bitmap: Bitmap,
        user: User,
        callback: (Result<String?>) -> Unit
    ){
        cloudinaryModel.uploadImage(
            bitmap = bitmap,
            name = user.email,
            onSuccess = { url ->
                if (!url.isNullOrBlank()) {
                    callback(Result.success(url))
                } else {
                    callback(Result.failure(Exception("Cloudinary URL is empty")))
                }},
            onError = { callback(Result.failure(Exception("Cloudinary upload failed"))) }
        )
    }


    fun refreshAllRecipes() {
        loadingState.postValue(LoadingState.LOADING)
        val lastUpdated: Long = Recipe.lastUpdated
        firebaseModel.getAllRecipes(lastUpdated) { recipes ->
            executer.execute {
                var currentTime = lastUpdated
                for (student in recipes) {
                    database.recipeDao().insertAll(student)
                    student.lastUpdated?.let {
                        if (currentTime < it) {
                            currentTime = it
                        }
                    }
                }

                Recipe.lastUpdated = currentTime
                loadingState.postValue(LoadingState.LOADED)
            }
        }
    }
}