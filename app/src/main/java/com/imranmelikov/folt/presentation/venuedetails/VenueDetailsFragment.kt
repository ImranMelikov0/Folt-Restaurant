package com.imranmelikov.folt.presentation.venuedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.databinding.FragmentVenueDetailsBinding

class VenueDetailsFragment : Fragment() {
    private lateinit var binding:FragmentVenueDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentVenueDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }
}