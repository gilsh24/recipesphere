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
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executors


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
    private val edamamModel = EdamamModel()
    val recipes: LiveData<List<Recipe>> = database.recipeDao().getAllRecipes()
    val loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()

    companion object {
        val shared = Model()
    }


    fun getCurrentUserRecipes(callback: RecipeCallback) {
        executer.execute {
            val id = FirebaseAuth.getInstance().currentUser?.uid
            if(id == null) {
                callback(emptyList())
            }
            id?.let {
                val recipes = database.recipeDao().getRecipesByUserId(id)
                Thread.sleep(3000)

                mainHandler.post {
                    callback(recipes)
                }
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

    fun updateUser(user: User, image: Bitmap?, callback: EmptyCallback) {
        firebaseModel.updateUser(user.uid, user.toMap()) {
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
        }
    }

    fun signOut() {
        firebaseModel.signOut()
    }

    fun addRecipe(recipe: Recipe, image: Bitmap?, callback: EmptyCallback) {
        firebaseModel.insertRecipe(recipe) {
            image?.let {
                cloudinaryModel.uploadImage(it, recipe.id, onSuccess = { uri ->
                    if (!uri.isNullOrBlank()) {
                        val updatedRecipe = recipe.copy(photoURL = uri)
                        firebaseModel.insertRecipe(updatedRecipe) { updateResult ->
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
        }
    }

    fun updateRecipe(recipe: Recipe, image: Bitmap?, callback: EmptyCallback) {
        firebaseModel.insertRecipe(recipe) { result ->
            if (result.isSuccess) {
                image?.let {
                    cloudinaryModel.uploadImage(it, recipe.id, onSuccess = { uri ->
                        if (!uri.isNullOrBlank()) {
                            val updatedRecipe = recipe.copy(photoURL = uri)
                            firebaseModel.insertRecipe(updatedRecipe) { updateResult ->
                                if (updateResult.isSuccess) {
                                    executer.execute {
                                        database.recipeDao().insertAll(updatedRecipe)
                                        mainHandler.post { callback() }
                                    }
                                } else {
                                    callback()
                                }
                            }
                        } else {
                            callback()
                        }
                    }, onError = { callback() })
                } ?: run {
                    executer.execute {
                        database.recipeDao().insertAll(recipe)
                        mainHandler.post { callback() }
                    }
                }
            } else {
                callback()
            }
        }
    }

    fun deleteRecipe(recipeId: String, callback: EmptyCallback) {
        firebaseModel.deleteRecipe(recipeId) { result ->
            if (result.isSuccess) {
                executer.execute {
                    try {
                        database.recipeDao().deleteById(recipeId)
                        mainHandler.post {
                            callback()
                        }
                    } catch (e: Exception) {
                        mainHandler.post {
                            callback()
                        }
                    }
                }
            } else {
                callback()
            }
        }
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

    fun getNutritionData(ingredients: List<String>, callback: (EdamamResponse?) -> Unit) {
        executer.execute {
            edamamModel.getNutritionFacts(ingredients) { response ->
                mainHandler.post {
                    callback(response)
                }
            }
        }
    }
}