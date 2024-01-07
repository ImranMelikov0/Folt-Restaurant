package com.imranmelikov.folt.presentation.restaurants

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentRestaurantBinding

class RestaurantFragment : Fragment() {
    private lateinit var binding:FragmentRestaurantBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRestaurantBinding.inflate(inflater,container,false)
        return binding.root
    }
}