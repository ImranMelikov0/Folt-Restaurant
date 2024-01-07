package com.imranmelikov.folt.presentation.banner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentBannerBinding

class BannerFragment : Fragment() {
    private lateinit var binding:FragmentBannerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBannerBinding.inflate(inflater,container,false)
        return binding.root
    }
}