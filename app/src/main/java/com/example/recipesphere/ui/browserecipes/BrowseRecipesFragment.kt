package com.example.recipesphere.ui.browserecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesphere.databinding.FragmentBrowseRecipesBinding
import com.example.recipesphere.model.Recipe
import com.example.recipesphere.ui.general.recipeslist.OnItemClickListener
import com.example.recipesphere.ui.general.recipeslist.RecipesRecyclerAdapter

class BrowseRecipesFragment : Fragment() {

    private var _binding: FragmentBrowseRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipesRecyclerAdapter
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

        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        adapter = RecipesRecyclerAdapter(viewModel.recipes.value, isMyRecipe = false)

        viewModel.recipes.observe(viewLifecycleOwner) {
            adapter.update(it)
            adapter.notifyDataSetChanged()
        }
        adapter.listener = object: OnItemClickListener {
            override fun onItemClick(recipe: Recipe?) {
                recipe?.let {
                    val action = BrowseRecipesFragmentDirections.actionNavigationAllRecipesToSingleRecipeFragment(it)
                    findNavController().navigate(action)
                }
            }
        }
        binding.rvRecipes.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getAllRecipes()
    }

    private fun getAllRecipes() {
//        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.refreshAllRecipes()
    }


}