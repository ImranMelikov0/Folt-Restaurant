package com.imranmelikov.folt.presentation.stores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentStoreBinding
import com.imranmelikov.folt.presentation.categories.VenueCategoryAdapter
import com.imranmelikov.folt.presentation.categories.VenueCategoryViewModel
import com.imranmelikov.folt.util.ArgumentConstants
import com.imranmelikov.folt.util.VenueCategoryConstants

class StoreFragment : Fragment() {
  private lateinit var binding:FragmentStoreBinding
    private lateinit var viewModel:StoreViewModel
    private lateinit var viewModelVenueCategory:VenueCategoryViewModel
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var venueCategoryAdapter: VenueCategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentStoreBinding.inflate(inflater,container,false)
        viewModel= ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        viewModelVenueCategory=ViewModelProvider(requireActivity())[VenueCategoryViewModel::class.java]

        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        viewModel.getVenues()
        viewModelVenueCategory.getStoreCategories()
        initialiseStoreRv()
        initialiseVenueCategoryRv()
        observeVenueCategories()
        observeVenues()
        clickSeeAllBtn()
    }
    private fun clickSeeAllBtn(){
        binding.seeAllBtn.setOnClickListener {
            val data = VenueCategoryConstants.Store
            val bundle = Bundle()
            bundle.putString(ArgumentConstants.venues, data)
            findNavController().navigate(R.id.action_storeFragment_to_categoriesFragment,bundle)
        }
    }
    private fun initialiseStoreRv(){
        storeAdapter= StoreAdapter()
        binding.storesRv.layoutManager= LinearLayoutManager(requireContext())
        binding.storesRv.adapter=storeAdapter
    }
    private fun initialiseVenueCategoryRv(){
        venueCategoryAdapter= VenueCategoryAdapter()
        binding.categoryRv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.categoryRv.adapter=venueCategoryAdapter
    }
    private fun observeVenues(){
        viewModel.venueLiveData.observe(viewLifecycleOwner) {
            storeAdapter.venueList=it
            venueCategoryAdapter.venueList=it
        }
    }
    private fun observeVenueCategories(){
        viewModelVenueCategory.storeCategoryLiveData.observe(viewLifecycleOwner) {
            venueCategoryAdapter.venueCategoryList=it
            venueCategoryAdapter.viewType= VenueCategoryConstants.Store
        }
    }
}