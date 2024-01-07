package com.imranmelikov.folt.presentation.itemsearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentItemSearchBinding

class ItemSearchFragment : Fragment() {
   private lateinit var binding:FragmentItemSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentItemSearchBinding.inflate(inflater,container,false)
        return binding.root
    }
}