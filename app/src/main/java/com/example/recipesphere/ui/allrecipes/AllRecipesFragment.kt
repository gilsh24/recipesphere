package com.example.recipesphere.ui.allrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipesphere.databinding.FragmentAllRecipesBinding

class AllRecipesFragment : Fragment() {

    private var _binding: FragmentAllRecipesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AllRecipesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllRecipesBinding.inflate(inflater, container, false)
        binding.text2.text = "All Recipes"
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