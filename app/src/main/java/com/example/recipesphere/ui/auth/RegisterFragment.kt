package com.example.recipesphere.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.recipesphere.R
import androidx.navigation.Navigation
import android.content.Intent
import com.example.recipesphere.MainActivity
import com.example.recipesphere.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(){

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        view.findViewById<Button>(R.id.registerButton).setOnClickListener {
            val email = view.findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = view.findViewById<EditText>(R.id.passwordEditText).text.toString()
            val firstName = view.findViewById<EditText>(R.id.firstNameEditText).text.toString()
            val lastName = view.findViewById<EditText>(R.id.lastNameEditText).text.toString()
            val age = view.findViewById<EditText>(R.id.ageEditText).text.toString().toInt()

            authViewModel.registerUser(email, password, firstName, lastName, age)
        }

        authViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Toast.makeText(requireContext(), "Registration Successful!", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_signInFragment)
            } else {
                Toast.makeText(requireContext(), "Error: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}