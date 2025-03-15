package com.example.recipesphere.model

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
                        val userData = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "age" to age,
                            "photoURL" to "default_avatar",
                            "email" to email,
                            "registrationDate" to com.google.firebase.Timestamp.now()
                        )

                        // create a new user entity in the users collection
                        database.collection("users").document(newUser.uid).set(userData)
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
}

