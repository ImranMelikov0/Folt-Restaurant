package com.imranmelikov.folt.presentation.storeitems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentStoreItemsBinding
import com.imranmelikov.folt.constants.StoreCategoryName
import com.imranmelikov.folt.constants.StoreCategoryTitle

class StoreItemsFragment : Fragment() {
   private lateinit var binding:FragmentStoreItemsBinding
   private lateinit var viewModel: StoreItemsViewModel
   private lateinit var storeMenuAdapter: StoreMenuAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentStoreItemsBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[StoreItemsViewModel::class.java]
        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        clickBackBtn()
        clickSearchBtn()
        viewModel.getStoreMenuList()
        initialiseStoreItemsRv()
        getControlArguments()
    }
    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun clickSearchBtn(){
        binding.searchBtn.setOnClickListener {
            findNavController().navigate(R.id.action_storeItemsFragment_to_itemSearchFragment)
        }
    }
    private fun getControlArguments(){
        val receiveStoreCategoryId=arguments?.getInt(StoreCategoryTitle.storeCategoryTitle)
        val receiveStoreCategoryName=arguments?.getString(StoreCategoryName.categoryName)
        receiveStoreCategoryName?.let {
            binding.storeCategoryName.text=it
        }
        receiveStoreCategoryId?.let {
            observeStoreMenuList(it)
        }
    }
    private fun observeStoreMenuList(id:Int){
        viewModel.storeMenuLiveData.observe(viewLifecycleOwner){venueDetails->
           val filteredList= venueDetails.filter { it.parentId==id }
            "${filteredList.size} Items".also { binding.storeItemsSize.text=it }
            storeMenuAdapter.storeMenuList=filteredList
        }
    }
    private fun initialiseStoreItemsRv(){
        storeMenuAdapter= StoreMenuAdapter()
        binding.storeItemsRv.layoutManager=GridLayoutManager(requireContext(),2)
        binding.storeItemsRv.adapter=storeMenuAdapter
    }
}