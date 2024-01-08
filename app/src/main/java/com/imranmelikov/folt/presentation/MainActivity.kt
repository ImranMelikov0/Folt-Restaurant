package com.imranmelikov.folt.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav:BottomNavigationView
    private lateinit var navController:NavController
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNav = binding.bottomNav
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)
//        NavigationUI.setupWithNavController(bottomNav,navController)
    }
}