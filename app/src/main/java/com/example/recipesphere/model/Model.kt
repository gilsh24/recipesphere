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

class Model {

    enum class LoadingState {
        LOADING,
        LOADED
    }

    private val database: AppLocalDbRepository = AppLocalDb.database
    private val executer = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebase = FirebaseModel()
    val recipes: LiveData<List<Recipe>> = database.recipeDao().getAllRecipes()
    val loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()

    companion object {
        val shared = Model()
    }

    fun insertRecipe(recipe: Recipe, image: Bitmap?, storage: Storage?, callback: EmptyCallback) {
        firebase.insertRecipe(recipe) {
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
        firebase.registerUser(email, password, firstName, lastName, age, callback)
    }

    fun signInUser(email: String, password: String, callback: (Result<Unit>) -> Unit) {
        firebase.signInUser(email, password, callback)
    }

    fun getUser(uid: String, callback: (Result<User>) -> Unit) {
        firebase.getUser(uid, callback)
    }

    fun isUserSignedIn(): Boolean {
        return firebase.isUserSignedIn()
    }

    fun updateUser(uid: String, updates: Map<String, Any>, callback: (Result<Unit>) -> Unit) {
        firebase.updateUser(uid, updates, callback)
    }

    fun signOut() {
        firebase.signOut()
    }

    fun refreshAllRecipes() {
        loadingState.postValue(LoadingState.LOADING)
        val lastUpdated: Long = Recipe.lastUpdated
        firebase.getAllStudents(lastUpdated) { recipes ->
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