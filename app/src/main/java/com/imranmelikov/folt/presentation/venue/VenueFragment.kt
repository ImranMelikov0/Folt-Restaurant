package com.imranmelikov.folt.presentation.venue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentVenueBinding

class VenueFragment : Fragment() {
  private lateinit var binding:FragmentVenueBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentVenueBinding.inflate(inflater,container,false)
        return binding.root
    }
}