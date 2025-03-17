package com.example.recipesphere.ui.browserecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipesphere.R
import com.example.recipesphere.databinding.FragmentBrowseRecipesBinding
import com.example.recipesphere.model.Model
import com.example.recipesphere.ui.general.recipeslist.RecipesListFragment

class BrowseRecipesFragment : Fragment() {

    private var _binding: FragmentBrowseRecipesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BrowseRecipesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowseRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Model.shared.getAllRecipes { recipes ->
            // Create an instance of RecipesListFragment with the recipes
            val recipesListFragment = RecipesListFragment.newInstance(recipes)

            // Add RecipesListFragment as a child fragment
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, recipesListFragment)
                .commit()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}