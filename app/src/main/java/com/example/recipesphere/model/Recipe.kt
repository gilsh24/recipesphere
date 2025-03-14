package com.example.recipesphere.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.recipesphere.base.Converters

//data class Recipe(
//    val userName: String,
//    val userAge: Int,
//    val title: String,
//    val description: String,
//    val ingredients: List<String>, // or Array<String>
//    val time: String,
//    val likes: Int,
//    val imageResId: String
//)


@Entity
data class Recipe(
    @PrimaryKey val id: String,
    val userName: String,
    val userAge: Int,
    val title: String,
    val description: String,
    @TypeConverters(Converters::class)
    val ingredients: List<String>,  // List of ingredients
    val time: String,
    val likes: Int,
    val imageResId: Int  // You can also use URLs if needed
//    val imageResId: Int  // You can also use URLs if needed
)