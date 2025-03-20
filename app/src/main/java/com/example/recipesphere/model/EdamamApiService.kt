package com.example.recipesphere.model

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EdamamApiService {
    @POST("api/nutrition-details")
    fun getNutritionDetails(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Body ingredients: IngredientsRequest
    ): Call<EdamamResponse>
}

data class IngredientsRequest(
    val ingr: List<String>
)