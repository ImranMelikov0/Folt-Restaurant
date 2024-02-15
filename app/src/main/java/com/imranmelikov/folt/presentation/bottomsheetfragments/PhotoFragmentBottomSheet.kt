package com.imranmelikov.folt.presentation.bottomsheetfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.imranmelikov.folt.databinding.FragmentPhotoBottomSheetBinding

class PhotoFragmentBottomSheet:BottomSheetDialogFragment() {
    private lateinit var binding:FragmentPhotoBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPhotoBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }
}