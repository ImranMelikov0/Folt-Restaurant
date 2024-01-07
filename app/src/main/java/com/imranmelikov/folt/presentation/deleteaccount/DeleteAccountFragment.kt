package com.imranmelikov.folt.presentation.deleteaccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentDeleteAccountBinding

class DeleteAccountFragment : Fragment() {
  private lateinit var binding:FragmentDeleteAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentDeleteAccountBinding.inflate(inflater,container,false)
        return binding.root
    }
}