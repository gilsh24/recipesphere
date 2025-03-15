package com.example.recipesphere.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipesphere.databinding.ActivityRegistrationBinding
import com.example.recipesphere.ui.register.RegistrationViewModel
import androidx.lifecycle.ViewModelProvider

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val age = binding.ageEditText.text.toString().toIntOrNull() ?: 0

            if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
                viewModel.register(email, password, firstName, lastName, age)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registrationResult.observe(this) { result ->
            if (result.isSuccess) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Registration failed: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}