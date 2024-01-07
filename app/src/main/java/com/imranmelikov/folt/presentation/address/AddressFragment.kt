package com.imranmelikov.folt.presentation.address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentAddressBinding

class AddressFragment : Fragment() {
   private lateinit var binding:FragmentAddressBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddressBinding.inflate(inflater,container,false)
        return binding.root
    }
}