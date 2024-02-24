package com.imranmelikov.folt.presentation.address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentAddressBinding
import com.imranmelikov.folt.presentation.MainActivity

class AddressFragment : Fragment() {
   private lateinit var binding:FragmentAddressBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddressBinding.inflate(inflater,container,false)
        clickBackBtn()
        return binding.root
    }

    private fun clickBackBtn(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                (activity as MainActivity).showBottomNav()
            }
        })
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
            (activity as MainActivity).showBottomNav()
        }
    }
}