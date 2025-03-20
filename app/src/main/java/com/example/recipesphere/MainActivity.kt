package com.example.recipesphere

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipesphere.databinding.ActivityMainBinding
import com.example.recipesphere.model.Model

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_recipes, R.id.navigation_profile,
                R.id.navigation_my_recipes,  R.id.navigation_add_recipe
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        Model.shared.loadingState.observe(this) { state ->
            binding.progressBar.visibility = when (state) {
                Model.LoadingState.LOADING -> android.view.View.VISIBLE
                Model.LoadingState.LOADED -> android.view.View.GONE
                null -> android.view.View.GONE
            }
        }
    }
}