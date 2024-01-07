package com.imranmelikov.folt.presentation.name

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentNameBinding

class NameFragment : Fragment() {
  private lateinit var binding:FragmentNameBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentNameBinding.inflate(inflater,container,false)
        return binding.root
    }
}