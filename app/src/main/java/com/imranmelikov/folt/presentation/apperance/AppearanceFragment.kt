package com.imranmelikov.folt.presentation.apperance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentAppearanceBinding

class AppearanceFragment : Fragment() {
  private lateinit var binding:FragmentAppearanceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentAppearanceBinding.inflate(inflater,container,false)
        return binding.root
    }
}