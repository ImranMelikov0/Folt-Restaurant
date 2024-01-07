package com.imranmelikov.folt.presentation.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
  private lateinit var binding:FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentAccountBinding.inflate(inflater,container,false)
        return binding.root
    }
}