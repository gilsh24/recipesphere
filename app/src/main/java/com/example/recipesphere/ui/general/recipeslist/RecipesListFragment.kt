package com.example.recipesphere.ui.general.recipeslist


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesphere.R
import com.example.recipesphere.databinding.FragmentRecipesListBinding
import com.example.recipesphere.model.Model
import com.example.recipesphere.model.Recipe

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!
    var recipes: List<Recipe>? = null
    private lateinit var adapter: RecipesRecyclerAdapter

    companion object {
        private const val ARG_RECIPES = "arg_recipes"
        fun newInstance(recipes: List<Recipe>): RecipesListFragment {
            val fragment = RecipesListFragment()
            val args = Bundle().apply {
                putParcelableArrayList(ARG_RECIPES, ArrayList(recipes))
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipes = arguments?.getParcelableArrayList<Recipe>(ARG_RECIPES)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecipesRecyclerAdapter(recipes)
        adapter.listener = object: OnItemClickListener {
            override fun onItemClick(recipe: Recipe?) {
                recipe.let {

//                val action =
                }
            }
        }
        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        adapter.update(recipes)
        adapter.notifyDataSetChanged()
    }

}
