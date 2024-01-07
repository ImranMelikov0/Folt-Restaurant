package com.imranmelikov.folt.presentation.stores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {
  private lateinit var binding:FragmentStoreBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentStoreBinding.inflate(inflater,container,false)
        return binding.root
    }
}