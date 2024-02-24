package com.imranmelikov.folt.presentation.orderhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentOrderHistoryBinding
import com.imranmelikov.folt.presentation.MainActivity

class OrderHistoryFragment : Fragment() {
   private lateinit var binding:FragmentOrderHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderHistoryBinding.inflate(inflater,container,false)
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

    private fun clickBrowseBtn(){
        ///////
        binding.saveBtn.setOnClickListener {
            findNavController().navigate(R.id.action_orderHistoryFragment_to_discoveryFragment)
            (activity as MainActivity).showBottomNav()
        }
    }
}