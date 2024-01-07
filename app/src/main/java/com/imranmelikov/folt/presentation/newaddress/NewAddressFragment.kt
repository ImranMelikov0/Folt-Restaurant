package com.imranmelikov.folt.presentation.newaddress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentNewAddressBinding

class NewAddressFragment : Fragment() {
   private lateinit var binding:FragmentNewAddressBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentNewAddressBinding.inflate(inflater,container,false)
        return binding.root
    }
}