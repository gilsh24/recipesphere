package com.example.recipesphere.ui.general.recipeslist


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesphere.databinding.FragmentRecipesListBinding
import com.example.recipesphere.model.Model
import com.example.recipesphere.model.Recipe

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!
    var recipes: List<Recipe>? = null
    private lateinit var adapter: RecipesRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        recipes = Model.shared.recipes

        adapter = RecipesRecyclerAdapter(recipes)
        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
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
        Model.shared.getAllRecipes {
            recipes = it
            Log.i("recipes getAllRecipes", recipes.toString()) // should have title description
            adapter.update(recipes)
            adapter.notifyDataSetChanged()
        }
    }
}
