package com.example.recipesphere.ui.auth

/*import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipesphere.databinding.ActivityRegistrationBinding
import androidx.lifecycle.ViewModelProvider
import com.example.recipesphere.MainActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel using ViewModelProvider:
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]

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

                // change to another page - like browse recipe
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Registration failed: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}*/