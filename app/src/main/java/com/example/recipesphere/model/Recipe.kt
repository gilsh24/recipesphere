package com.example.recipesphere.model

import android.content.Context
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.recipesphere.base.Converters
import com.example.recipesphere.base.MyApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Recipe(
    @PrimaryKey val id: String,
    val userId: String,
    val userName: String,
    val userAge: Int,
    val title: String,
    val difficultyLevel: Int,
    val instructions: String,
    @TypeConverters(Converters::class)
    val ingredients: List<String>,  // List of ingredients
    val time: String,
    val likes: Int,
    val imageResId: Int,
    val photoURL: String = "",
    val lastUpdated: Long? = null,
    val calories: Double = 0.0,
    @TypeConverters(Converters::class)
    val dietLabels: List<String> = emptyList(),
    @TypeConverters(Converters::class)
    val healthLabels: List<String> = emptyList(),
    @TypeConverters(Converters::class)
    val cautions: List<String> = emptyList(),
    @TypeConverters(Converters::class)
    val mealType: List<String>? = null,
    @TypeConverters(Converters::class)
    val cuisineType: List<String>? = null
) : Parcelable {
    companion object {
        const val LOCAL_LAST_UPDATED = "localRecipeLastUpdated"

        var lastUpdated: Long
            get() = MyApplication.Globals.context?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                ?.getLong(LOCAL_LAST_UPDATED, 0) ?: 0

            set(value) {
                MyApplication.Globals.context
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.apply {
                        edit().putLong(LOCAL_LAST_UPDATED, value).apply()
                    }
            }

        const val ID_KEY = "id"
        const val USER_NAME_KEY = "userName"
        const val INSTRUCTIONS_KEY = "instructions"
        const val USER_ID_KEY = "userID"
        const val USER_AGE_KEY = "userAge"
        const val TITLE_KEY = "title"
        const val DIFFICULTY_LEVEL_KEY = "difficultyLevel"
        const val INGREDIENTS_KEY = "ingredients"
        const val TIME_KEY = "time"
        const val LIKES_KEY = "likes"
        const val IMAGE_RES_ID_KEY = "imageResId"
        const val PHOTO_URL_KEY = "photoURL"
        const val LAST_UPDATED_KEY = "lastUpdated"
        const val CALORIES_KEY = "calories"
        const val DIET_LABELS_KEY = "dietLabels"
        const val HEALTH_LABELS_KEY = "healthLabels"
        const val CAUTIONS_KEY = "cautions"
        const val MEAL_TYPE_KEY = "mealType"
        const val CUISINE_TYPE_KEY = "cuisineType"

        fun fromJSON(json: Map<String, Any>): Recipe {
            val id = json[ID_KEY] as? String ?: ""
            val userName = json[USER_NAME_KEY] as? String ?: ""
            val instructions = json[INSTRUCTIONS_KEY] as? String ?: ""
            val userId = json[USER_ID_KEY] as? String ?: ""
            val userAge = when(val age = json[USER_AGE_KEY]) {
                is Long -> age.toInt()
                is Int -> age
                is Double -> age.toInt()
                else -> 0
            }
            val title = json[TITLE_KEY] as? String ?: ""
            val difficultyLevel = when(val level = json[DIFFICULTY_LEVEL_KEY]) {
                is Long -> level.toInt()
                is Int -> level
                is Double -> level.toInt()
                else -> 0
            }
            val ingredients = json[INGREDIENTS_KEY] as? List<*> ?: emptyList<Any>()
            // Ensure that each ingredient is a String
            val ingredientsList = ingredients.map { it.toString() }
            val time = json[TIME_KEY] as? String ?: ""
            val likes = when(val likesVal = json[LIKES_KEY]) {
                is Long -> likesVal.toInt()
                is Int -> likesVal
                is Double -> likesVal.toInt()
                else -> 0
            }
            val imageResId = when(val imgVal = json[IMAGE_RES_ID_KEY]) {
                is Long -> imgVal.toInt()
                is Int -> imgVal
                is Double -> imgVal.toInt()
                else -> 0
            }
            val photoURL = json[PHOTO_URL_KEY] as? String ?: ""
            val timeStamp = json[LAST_UPDATED_KEY] as? Timestamp
            val lastUpdatedLongTimestamp = timeStamp?.toDate()?.time
            val calories = (json[CALORIES_KEY] as? Number)?.toDouble() ?: 0.0
            val dietLabels = (json[DIET_LABELS_KEY] as? List<*>)?.map { it.toString() } ?: emptyList()
            val healthLabels = (json[HEALTH_LABELS_KEY] as? List<*>)?.map { it.toString() } ?: emptyList()
            val cautions = (json[CAUTIONS_KEY] as? List<*>)?.map { it.toString() } ?: emptyList()
            val mealType = (json[MEAL_TYPE_KEY] as? List<*>)?.map { it.toString() }
            val cuisineType = (json[CUISINE_TYPE_KEY] as? List<*>)?.map { it.toString() }


            return Recipe(
                id = id,
                userName = userName,
                userAge = userAge,
                title = title,
                difficultyLevel = difficultyLevel,
                ingredients = ingredientsList,
                time = time,
                likes = likes,
                imageResId = imageResId,
                photoURL = photoURL,
                lastUpdated = lastUpdatedLongTimestamp,
                userId = userId,
                instructions = instructions,
                calories = calories,
                dietLabels = dietLabels,
                healthLabels = healthLabels,
                cautions = cautions,
                mealType = mealType,
                cuisineType = cuisineType
            )
        }

    }

    val json: Map<String, Any>
        get() = hashMapOf(
            ID_KEY to id,
            USER_NAME_KEY to userName,
            USER_AGE_KEY to userAge,
            TITLE_KEY to title,
            DIFFICULTY_LEVEL_KEY to difficultyLevel,
            INGREDIENTS_KEY to ingredients,
            TIME_KEY to time,
            LIKES_KEY to likes,
            IMAGE_RES_ID_KEY to imageResId,
            PHOTO_URL_KEY to photoURL,
            LAST_UPDATED_KEY to FieldValue.serverTimestamp(),
            INSTRUCTIONS_KEY to instructions,
            USER_ID_KEY to userId,
            CALORIES_KEY to calories,
            DIET_LABELS_KEY to dietLabels,
            HEALTH_LABELS_KEY to healthLabels,
            CAUTIONS_KEY to cautions,
            MEAL_TYPE_KEY to (mealType ?: emptyList<String>()),
            CUISINE_TYPE_KEY to (cuisineType ?: emptyList<String>())
        )

}