package com.example.recipesphere.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipesphere.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(){

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val age = binding.ageEditText.text.toString().toIntOrNull()

            if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && age != null) {
                authViewModel.registerUser(email, password, firstName, lastName, age)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            if (isLoading){
                binding.progressBar.visibility = View.VISIBLE // Example using a ProgressBar
                binding.registerButton.isEnabled = false
            } else {
                binding.progressBar.visibility = View.GONE
                binding.registerButton.isEnabled = true
            }
        }

        authViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Toast.makeText(requireContext(), "Registration Successful!", Toast.LENGTH_SHORT).show()
                // Go back to Sign In
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Error: Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }
}