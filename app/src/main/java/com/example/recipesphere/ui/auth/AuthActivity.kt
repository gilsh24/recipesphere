package com.example.recipesphere.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.recipesphere.R
import com.example.recipesphere.ui.auth.RegisterFragment
import com.example.recipesphere.ui.auth.SignInFragment


class AuthActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Load the RegisterFragment by default
        if (savedInstanceState == null) {
            loadFragment(RegisterFragment())
        }
    }

    // Function to load a fragment into the container
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.authFragmentContainer, fragment)
            .addToBackStack(null)  // Allows back navigation
            .commit()
    }

    // Function to navigate to SignInFragment
    fun navigateToSignIn() {
        loadFragment(SignInFragment())
    }

    // Function to navigate to RegisterFragment
    fun navigateToRegister() {
        loadFragment(RegisterFragment())
    }

}