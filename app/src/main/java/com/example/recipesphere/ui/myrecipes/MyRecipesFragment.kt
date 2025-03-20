package com.example.recipesphere.ui.myrecipes

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesphere.databinding.FragmentMyRecipesBinding
import com.example.recipesphere.model.Model
import com.example.recipesphere.model.Recipe
import com.example.recipesphere.ui.general.recipeslist.OnItemClickListener
import com.example.recipesphere.ui.general.recipeslist.RecipesRecyclerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyRecipesFragment : Fragment() {

    private var _binding: FragmentMyRecipesBinding? = null
    private val binding get() = _binding!!
//    private val viewModel: MyRecipesViewModel by viewModels()
    var recipes: List<Recipe>? = null
    private lateinit var adapter: RecipesRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        adapter = RecipesRecyclerAdapter(recipes, isMyRecipe = true)

        adapter.listener = object: OnItemClickListener {
            override fun onItemClick(recipe: Recipe?) {
                recipe?.let {
                    val action = MyRecipesFragmentDirections.actionNavigationMyRecipesToSingleRecipeFragment(it)
                    findNavController().navigate(action)
                }
            }

            override fun onEditClick(recipe: Recipe?) {
                recipe?.let {
                    val action = MyRecipesFragmentDirections.actionNavigationMyRecipesToAddRecipeFragment(it)
                    findNavController().navigate(action)
                }
            }

            override fun onDeleteClick(recipe: Recipe?) {
                recipe?.let {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Delete Recipe")
                        .setMessage("Are you sure you want to delete ${recipe.title}?")
                        .setPositiveButton("Yes") { _, _ ->
                            CoroutineScope(Dispatchers.Main).launch {
                                Model.shared.deleteRecipe(recipe.id) {
                                    getMyRecipes()
                                }
                            }
                        }
                        .setNegativeButton("No", null)
                        .show()
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
        getMyRecipes()
    }

    private fun getMyRecipes() {
//        binding?.progressBar?.visibility = View.VISIBLE
//        viewModel.refreshAllStudents()

        Model.shared.getCurrentUserRecipes  {
            recipes = it
            adapter.update(it)
            adapter.notifyDataSetChanged()
        }
    }
}