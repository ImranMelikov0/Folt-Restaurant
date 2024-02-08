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
import com.imranmelikov.folt.presentation.venue.VenueAdapter
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.VenueCategoryConstants
import com.imranmelikov.folt.presentation.venue.VenueViewModel

class StoreFragment : Fragment() {
  private lateinit var binding:FragmentStoreBinding
    private lateinit var viewModel:VenueViewModel
    private lateinit var viewModelVenueCategory:VenueCategoryViewModel
    private lateinit var venueAdapter: VenueAdapter
    private lateinit var venueCategoryAdapter: VenueCategoryAdapter
    private lateinit var bundle:Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentStoreBinding.inflate(inflater,container,false)
        viewModel= ViewModelProvider(requireActivity())[VenueViewModel::class.java]
        viewModelVenueCategory=ViewModelProvider(requireActivity())[VenueCategoryViewModel::class.java]
        bundle=Bundle()
        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        viewModel.getVenues()
        viewModelVenueCategory.getVenueCategories()
        initialiseStoreRv()
        initialiseVenueCategoryRv()
        observeVenueCategories()
        observeVenues()
        clickSeeAllBtn()
    }
    private fun clickSeeAllBtn(){
        binding.seeAllBtn.setOnClickListener {
            findNavController().navigate(R.id.action_storeFragment_to_categoriesFragment,bundle)
        }
    }
    private fun initialiseStoreRv(){
        venueAdapter= VenueAdapter()
        binding.storesRv.layoutManager= LinearLayoutManager(requireContext())
        binding.storesRv.adapter=venueAdapter
    }
    private fun initialiseVenueCategoryRv(){
        venueCategoryAdapter= VenueCategoryAdapter()
        binding.categoryRv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.categoryRv.adapter=venueCategoryAdapter
    }
    private fun observeVenues(){
        viewModel.venueLiveData.observe(viewLifecycleOwner) {venueList->
            val filteredVenueList=venueList.filter { !it.restaurant }
            venueAdapter.venueList=filteredVenueList
            venueAdapter.viewType=VenueCategoryConstants.Store
            venueCategoryAdapter.venueList=filteredVenueList
            bundle.apply {
                putString(VenueCategoryConstants.VenueCategoryTitle,VenueCategoryConstants.VenueCategoryTitle)
                putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
            }
        }
    }
    private fun observeVenueCategories(){
        viewModelVenueCategory.venueCategoryLiveData.observe(viewLifecycleOwner) {venueCategoryList->
            val filteredCategoryList=venueCategoryList.filter { !it.restaurant }
            venueCategoryAdapter.venueCategoryList=filteredCategoryList
            venueCategoryAdapter.viewType= VenueCategoryConstants.Store
            bundle.apply {
                putString(VenueCategoryConstants.VenueCategoryTitle,VenueCategoryConstants.VenueCategoryTitle)
                putSerializable(VenueCategoryConstants.venueCategories, ArrayList(filteredCategoryList))
            }
        }
    }
}