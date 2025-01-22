package com.example.recipesphere.ui.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.recipesphere.databinding.FragmentAddRecipeBinding
import com.example.recipesphere.ui.myrecipes.MyRecipesViewModel

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyRecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addRecipeViewModel =
            ViewModelProvider(this)[AddRecipeViewModel::class.java]
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        binding.text2.text = "Add Recipe"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}