package com.example.recipesphere.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesphere.model.Recipe

@Dao
interface RecipeDao {

    @Query("SELECT * FROM Recipe")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM Recipe WHERE id =:id")
    fun getRecipeById(id: String): Recipe?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)
}