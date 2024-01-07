package com.imranmelikov.folt.presentation.addressdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentAddressDetailsBinding

class AddressDetailsFragment : Fragment() {
  private lateinit var binding:FragmentAddressDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddressDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }
}