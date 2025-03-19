package com.example.recipesphere.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.recipesphere.base.Converters
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Recipe(
    @PrimaryKey val id: String,
    val userName: String,
    val userAge: Int,
    val title: String,
    val difficultyLevel: Int,
    @TypeConverters(Converters::class)
    val ingredients: List<String>,  // List of ingredients
    val time: String,
    val likes: Int,
    val imageResId: Int
) : Parcelable