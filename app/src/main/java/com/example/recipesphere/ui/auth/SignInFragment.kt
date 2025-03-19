package com.example.recipesphere.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.recipesphere.MainActivity
import com.example.recipesphere.R
import androidx.navigation.fragment.findNavController
import com.example.recipesphere.databinding.FragmentSigninBinding

class SignInFragment : Fragment(){
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: FragmentSigninBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.loginUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.goToRegisterButton.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_registerFragment)
        }

        authViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            if (isLoading){
                binding.progressBar.visibility = View.VISIBLE
                binding.signInButton.isEnabled = false
            } else {
                binding.progressBar.visibility = View.GONE
                binding.signInButton.isEnabled = true
            }
        }

        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Toast.makeText(requireContext(), "SignIn Successful!", Toast.LENGTH_SHORT).show()
                // Navigate to Main activity
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Error: SignIn Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
