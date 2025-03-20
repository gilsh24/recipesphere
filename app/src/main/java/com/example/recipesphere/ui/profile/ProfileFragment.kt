package com.example.recipesphere.ui.profile

import android.content.Intent
import androidx.fragment.app.viewModels
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.recipesphere.AuthActivity
import com.example.recipesphere.MainActivity
import com.example.recipesphere.databinding.FragmentProfileBinding
import com.example.recipesphere.model.Model
import com.example.recipesphere.model.User
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var didSetProfileImage = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.saveButton.setOnClickListener(::onSaveClicked)
        binding.cancelButton.setOnClickListener(::onCancelClick)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            binding.imageView.setImageBitmap(bitmap)
            didSetProfileImage = true
        }

        binding.takePhotoButton.setOnClickListener {
            cameraLauncher?.launch(null)
        }

        binding.logoutButton.setOnClickListener{
            Model.shared.signOut()
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner){user ->
            if(user != null){
                binding.firstNameTextView.setText(user.firstName)
                binding.lastNameTextView.setText(user.lastName)
                binding.ageTextView.setText(String.format(user.age.toString()))
                binding.emailAddrView.text = user.email

                if (!user.photoURL.isNullOrEmpty() && user.photoURL != "default_avatar"){
                    Picasso.get().load(user.photoURL).into(binding.imageView)
                } else {
                    binding.imageView.setImageResource(com.example.recipesphere.R.drawable.avatar)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
            binding.saveButton.isEnabled = !isLoading
            binding.cancelButton.isEnabled = !isLoading
        }

        viewModel.updateResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.isSuccess) {
                    Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                    viewModel.getUser()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Profile Update Failed: ${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.getUser()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onSaveClicked(view: View) {
        val firstName = binding.firstNameTextView.text.toString()
        val lastName = binding.lastNameTextView.text.toString()
        val age = binding.ageTextView.text.toString()

        val user = User(
            uid = FirebaseAuth.getInstance().currentUser?.uid ?: "",
            firstName = firstName,
            lastName = lastName,
            age = age.toInt(),
            email = "",
            photoURL = ""
        )

        binding.progressBar.visibility = View.VISIBLE

        Model.shared.updateUser(user, if (didSetProfileImage) (binding.imageView.drawable as BitmapDrawable).bitmap else null) {
            binding.progressBar.visibility = View.GONE
            if(didSetProfileImage){
                didSetProfileImage = false;
            }

            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()

            viewModel.getUser()
        }
    }

    private fun onCancelClick(view: View) {
        Navigation.findNavController(view).popBackStack()
    }
}