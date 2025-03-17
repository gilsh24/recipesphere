package com.example.recipesphere.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.recipesphere.MainActivity
import com.example.recipesphere.R
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.recipesphere.databinding.FragmentRegisterBinding
import com.example.recipesphere.databinding.FragmentSigninBinding

class SignInFragment : Fragment(){
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: FragmentSigninBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        view.findViewById<Button>(R.id.signInButton).setOnClickListener {
            val email = view.findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = view.findViewById<EditText>(R.id.passwordEditText).text.toString()

            authViewModel.loginUser(email, password)
        }

        view.findViewById<Button>(R.id.goToRegisterButton).setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_registerFragment)
//            Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_registerFragment)
        }

        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Toast.makeText(requireContext(), "SignIn Successful!", Toast.LENGTH_SHORT).show()
                // Navigate to Main activity
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Error: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
