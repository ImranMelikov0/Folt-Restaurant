package com.imranmelikov.folt.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.AppearanceConstants
import com.imranmelikov.folt.constants.FireStoreCollectionConstants
import com.imranmelikov.folt.databinding.ActivityMainBinding
import com.imranmelikov.folt.languagemanager.LanguageManager
import com.imranmelikov.folt.presentation.venuedetails.VenueDetailsViewModel
import com.imranmelikov.folt.sharedpreferencesmanager.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav:BottomNavigationView
    private lateinit var navController:NavController
    private lateinit var binding:ActivityMainBinding
    private lateinit var viewModel: VenueDetailsViewModel
    @Inject lateinit var sharedPreferencesManager: SharedPreferencesManager
    @Inject lateinit var auth: FirebaseAuth
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this)[VenueDetailsViewModel::class.java]

        controlAppearance()
        changeLanguage()
        signOut()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        bottomNav = binding.bottomNav
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)
//        NavigationUI.setupWithNavController(bottomNav,navController)
    }
    fun hideBottomNav(){
        binding.bottomNav.visibility=View.GONE
    }
    fun showBottomNav(){
        binding.bottomNav.visibility=View.VISIBLE
    }

    private fun controlAppearance(){
        when(sharedPreferencesManager.load(AppearanceConstants.theme,"")){
            AppearanceConstants.light->{
            }
            AppearanceConstants.dark->{
            }
            AppearanceConstants.default->{
            }
        }
    }

    private fun changeLanguage(){
       val languageCode= sharedPreferencesManager.load(FireStoreCollectionConstants.language,"")
        LanguageManager.updateLanguage(this,languageCode)
    }

    private fun signOut(){
        if (auth.currentUser==null){
            auth.signOut()
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
        viewModel.deleteAllVenueDetailsFromRoom()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.deleteAllVenueDetailsFromRoom()
    }

}