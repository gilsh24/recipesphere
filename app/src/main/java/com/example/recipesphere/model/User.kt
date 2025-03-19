package com.example.recipesphere.model

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val age: Int = 0,
    val email: String = "",
    val photoURL: String = "default_avatar",
    val registrationDate: Long = System.currentTimeMillis()
)