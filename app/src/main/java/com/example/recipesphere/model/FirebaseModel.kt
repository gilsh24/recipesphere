package com.example.recipesphere.model

import android.util.Log
import com.example.recipesphere.base.Constants
import com.example.recipesphere.base.EmptyCallback
import com.example.recipesphere.base.RecipeCallback
import com.example.recipesphere.utils.extensions.toFirebaseTimestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel {

    private val database = Firebase.firestore
    private val auth = Firebase.auth

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
        }
        database.firestoreSettings = settings

    }

    fun registerUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        age: Int,
        callback: (Result<Unit>) -> Unit
    ) {

        // create a new User with email and password auth in firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    val newUser = auth.currentUser
                    if (newUser != null) {
                        val user = User(
                            uid = newUser.uid,
                            firstName = firstName,
                            lastName = lastName,
                            age = age,
                            email = email
                        )

                        // create a new user entity in the users collection
                        database.collection("users").document(newUser.uid).set(user)
                            .addOnCompleteListener { firestoreResult ->
                                if (firestoreResult.isSuccessful) {
                                    callback(Result.success(Unit))
                                } else {
                                    callback(Result.failure(firestoreResult.exception ?: Exception("Firestore adding user to db failed")))
                                }
                            }
                    } else {
                        callback(Result.failure(Exception("User creation in Firebase failed")))
                    }
                } else {
                    callback(Result.failure(authResult.exception ?: Exception("Auth failed")))
                }
            }
    }

    fun signInUser(email: String, password: String, callback: (Result<Unit>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    callback(Result.success(Unit))
                } else {
                    callback(Result.failure(authResult.exception ?: Exception("Sign-in failed")))
                }
            }
    }

    fun getUser(uid: String, callback: (Result<User>) -> Unit) {
        database.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                if (user != null) {
                    callback(Result.success(user))
                } else {
                    callback(Result.failure(Exception("User not found")))
                }
            }
            .addOnFailureListener { callback(Result.failure(it)) }
    }

    fun isUserSignedIn(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }

    fun updateUser(uid: String, updates: Map<String, Any>, callback: (Result<Unit>) -> Unit) {
        database.collection("users").document(uid)
            .update(updates)
            .addOnSuccessListener { callback(Result.success(Unit)) }
            .addOnFailureListener { callback(Result.failure(it)) }
    }

    fun signOut() {
        auth.signOut()
    }

    fun insertRecipe(recipe: Recipe, callback: EmptyCallback) {
        database.collection(Constants.Collections.RECIPES).document(recipe.id).set(recipe.json)
            .addOnCompleteListener {
                callback()
            }
            .addOnFailureListener {
                Log.d("TAG", it.toString() + it.message)
            }
    }

    fun getAllStudents(sinceLastUpdated: Long, callback: RecipeCallback) {
        database.collection(Constants.Collections.RECIPES)
            .whereGreaterThanOrEqualTo(Recipe.LAST_UPDATED_KEY, sinceLastUpdated.toFirebaseTimestamp)
            .get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val recipes: MutableList<Recipe> = mutableListOf()
                        for (json in it.result) {
                            recipes.add(Recipe.fromJSON(json.data))
                        }
                        Log.d("TAG", recipes.size.toString())
                        callback(recipes)
                    }

                    false -> callback(listOf())
                }
            }
    }
}

