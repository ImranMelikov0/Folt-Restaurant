package com.imranmelikov.folt.presentation.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {
   private lateinit var binding:FragmentCategoriesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCategoriesBinding.inflate(inflater,container,false)
        return binding.root
    }
}