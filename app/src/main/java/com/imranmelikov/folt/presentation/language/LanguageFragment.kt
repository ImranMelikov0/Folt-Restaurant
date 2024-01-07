package com.imranmelikov.folt.presentation.language

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentLanguageBinding
class LanguageFragment : Fragment() {
    private lateinit var binding:FragmentLanguageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentLanguageBinding.inflate(inflater,container,false)
        return binding.root
    }
}