package com.example.recipesphere.model

data class EdamamResponse(
    val calories: Double,
    val dietLabels: List<String>,
    val healthLabels: List<String>,
    val cuisineType: List<String>?,
    val mealType: List<String>?,
    val cautions: List<String>
)