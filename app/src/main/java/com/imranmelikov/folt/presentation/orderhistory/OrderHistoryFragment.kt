package com.imranmelikov.folt.presentation.orderhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentOrderHistoryBinding

class OrderHistoryFragment : Fragment() {
   private lateinit var binding:FragmentOrderHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }
}