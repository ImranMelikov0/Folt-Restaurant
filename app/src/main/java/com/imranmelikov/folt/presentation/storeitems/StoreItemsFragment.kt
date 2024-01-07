package com.imranmelikov.folt.presentation.storeitems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentStoreItemsBinding

class StoreItemsFragment : Fragment() {
   private lateinit var binding:FragmentStoreItemsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentStoreItemsBinding.inflate(inflater,container,false)
        return binding.root
    }
}