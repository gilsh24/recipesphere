package com.example.recipesphere.model

import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.recipesphere.R
import com.example.recipesphere.base.RecipeCallback
import com.example.recipesphere.model.dao.AppLocalDb
import com.example.recipesphere.model.dao.AppLocalDbRepository
import java.util.concurrent.Executors

class Model {

    private val database: AppLocalDbRepository = AppLocalDb.database
    private val executer = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebase = FirebaseModel()

    companion object {
        val shared = Model()
    }

    init {
        val recipes = mutableListOf<Recipe>()
        for (i in 0..5) {
            val recipe = Recipe(
                id = "$i",
                userName = "User $i",
                userAge = 20 + i,
                title = "Recipe $i",
                difficultyLevel = 3,
                ingredients = ArrayList(2),
                time = "${20 + i} minutes",
                likes = i * 5,
                imageResId = R.drawable.pancakes
            )
            recipes.add(recipe)
        }
        val recipesArray = recipes.toTypedArray()
        executer.execute {
            database.recipeDao().insertAll(*recipesArray)
        }
    }

    fun getAllRecipes(callback: RecipeCallback) {
        executer.execute {
            val recipes = database.recipeDao().getAllRecipes()

            Thread.sleep(3000)

            mainHandler.post {
                callback(recipes)
            }
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
}