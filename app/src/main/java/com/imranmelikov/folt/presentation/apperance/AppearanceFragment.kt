package com.imranmelikov.folt.presentation.apperance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentAppearanceBinding

class AppearanceFragment : Fragment() {
  private lateinit var binding:FragmentAppearanceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentAppearanceBinding.inflate(inflater,container,false)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        controlTheme()
        return binding.root
    }

    private fun controlTheme(){
        /// add shared preferences
        binding.customImageButton.setOnClickListener {
            binding.customImageButton.setImageResource(R.drawable.baseline_check_circle_24)
            binding.customImageButton2.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchCompat.isChecked = false
        }
        binding.customImageButton2.setOnClickListener {
            binding.customImageButton2.setImageResource(R.drawable.baseline_check_circle_24)
            binding.customImageButton.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.switchCompat.isChecked = false
        }

        binding.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                binding.customImageButton.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
                binding.customImageButton2.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
            } 
        }
    }
}