package com.imranmelikov.folt.presentation.orderdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentOrderDetailBinding

class OrderDetailFragment : Fragment() {
  private lateinit var binding:FragmentOrderDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentOrderDetailBinding.inflate(inflater,container,false)
        return binding.root
    }
}