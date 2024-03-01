package com.imranmelikov.folt.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imranmelikov.folt.constants.FireStoreCollectionConstants
import com.imranmelikov.folt.databinding.ActivityLoginBinding
import com.imranmelikov.folt.languagemanager.LanguageManager
import com.imranmelikov.folt.sharedpreferencesmanager.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        changeLanguage()
        setContentView(binding.root)
    }

    private fun changeLanguage(){
        val languageCode= sharedPreferencesManager.load(FireStoreCollectionConstants.language,"")
        LanguageManager.updateLanguage(this,languageCode)
    }
}