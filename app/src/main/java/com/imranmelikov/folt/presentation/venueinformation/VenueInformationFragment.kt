package com.imranmelikov.folt.presentation.venueinformation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentVenueInformationBinding

class VenueInformationFragment : Fragment() {
    private lateinit var binding:FragmentVenueInformationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentVenueInformationBinding.inflate(inflater,container,false)
        return binding.root
    }
}