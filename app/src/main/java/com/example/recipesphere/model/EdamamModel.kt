package com.example.recipesphere.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.recipesphere.BuildConfig


class EdamamModel {

    private val appId = BuildConfig.EDAMAM_APP_ID
    private val appKey = BuildConfig.EDAMAM_APP_KEY
    private val baseUrl = "https://api.edamam.com/"

    private val apiService: EdamamApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        apiService = retrofit.create(EdamamApiService::class.java)
    }

    fun getNutritionFacts(ingredients: List<String>, callback: (EdamamResponse?) -> Unit) {
        val requestBody = IngredientsRequest(ingredients)
        val call = apiService.getNutritionDetails(appId, appKey, requestBody)

        call.enqueue(object : Callback<EdamamResponse> {
            override fun onResponse(call: Call<EdamamResponse>, response: Response<EdamamResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<EdamamResponse>, t: Throwable) {
                t.printStackTrace()
                callback(null)
            }
        })
    }
}