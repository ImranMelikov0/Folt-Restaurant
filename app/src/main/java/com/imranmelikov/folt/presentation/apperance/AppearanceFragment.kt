package com.imranmelikov.folt.presentation.apperance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.AppearanceConstants
import com.imranmelikov.folt.databinding.FragmentAppearanceBinding
import com.imranmelikov.folt.sharedpreferencesmanager.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppearanceFragment : Fragment() {
  private lateinit var binding:FragmentAppearanceBinding
  @Inject lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentAppearanceBinding.inflate(inflater,container,false)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        controlTheme()
        controlAppearance()
        return binding.root
    }
    private fun controlAppearance(){
        when(sharedPreferencesManager.load(AppearanceConstants.theme,"")){
            AppearanceConstants.light->{
                light()
            }
            AppearanceConstants.dark->{
              dark()
            }
            AppearanceConstants.default->{
                default()
                binding.switchCompat.isChecked=true
            }
            else->{
                binding.switchCompat.isChecked=true
            }
        }
    }
    private fun light(){
        binding.customImageButton.setImageResource(R.drawable.baseline_check_circle_24)
        binding.customImageButton2.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
        binding.switchCompat.isChecked = false
    }
    private fun dark(){
        binding.customImageButton2.setImageResource(R.drawable.baseline_check_circle_24)
        binding.customImageButton.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
        binding.switchCompat.isChecked = false
    }
    private fun default(){
        binding.customImageButton.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
        binding.customImageButton2.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
    }

    private fun controlTheme(){
        binding.customImageButton.setOnClickListener {
            sharedPreferencesManager.save(AppearanceConstants.theme,AppearanceConstants.light)
             light()
        }
        binding.customImageButton2.setOnClickListener {
            sharedPreferencesManager.save(AppearanceConstants.theme,AppearanceConstants.dark)
            dark()
        }

        binding.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                sharedPreferencesManager.save(AppearanceConstants.theme,AppearanceConstants.default)
                default()
            }
        }
    }
}