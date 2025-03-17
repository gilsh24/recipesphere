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
//    val recipes: MutableList<Recipe> = ArrayList()

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
                description = "This is recipe number $i",
                ingredients = ArrayList(2),
                time = "${20 + i} minutes",
                likes = i * 5,
                imageResId = R.drawable.pancakes
            )
            recipes.add(recipe)
//            recipes.add(recipe)
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
}