package com.imranmelikov.folt.presentation.address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.AddressConstants
import com.imranmelikov.folt.databinding.FragmentAddressBinding
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.util.Status

class AddressFragment : Fragment() {
   private lateinit var binding:FragmentAddressBinding
   private lateinit var adapter: AddressAdapter
   private lateinit var viewModel: AddressViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddressBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[AddressViewModel::class.java]
        viewModel.getAddress("a")
        clickBackBtn()
        initializeRv()
        observeAddress()
        clickNewAddressBtn()
        return binding.root
    }

    private fun clickBackBtn(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_addressFragment_to_profileFragment)
                (activity as MainActivity).showBottomNav()
            }
        })
        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addressFragment_to_profileFragment)
            (activity as MainActivity).showBottomNav()
        }
    }
    private fun clickNewAddressBtn(){
        binding.addNewAddressBtn.setOnClickListener {
            val bundle=Bundle()
            bundle.apply {
                putString(AddressConstants.newAddressBundleKey,AddressConstants.addressNewAddress)
            }
            findNavController().navigate(R.id.action_addressFragment_to_newAddressFragment,bundle)
        }
    }
    private fun initializeRv(){
        adapter= AddressAdapter()
        binding.addressRv.layoutManager=LinearLayoutManager(requireContext())
        binding.addressRv.adapter=adapter
    }
    private fun observeAddress(){
        viewModel.addressLiveData.observe(viewLifecycleOwner){
            when(it.status){
                Status.ERROR->{
                    binding.progress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
                Status.SUCCESS->{
                    it.data?.let { addressList->
                        adapter.addressList=addressList
                        binding.progress.visibility=View.GONE
                        binding.noResultText.visibility=View.GONE
                    }
                }
                Status.LOADING->{
                    binding.progress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
            }
        }
    }
}