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
import com.imranmelikov.folt.util.VenueConstants
import com.imranmelikov.folt.util.VenueCategoryConstants

class StoreFragment : Fragment() {
  private lateinit var binding:FragmentStoreBinding
    private lateinit var viewModel:StoreViewModel
    private lateinit var viewModelVenueCategory:VenueCategoryViewModel
    private lateinit var venueAdapter: VenueAdapter
    private lateinit var venueCategoryAdapter: VenueCategoryAdapter
    private lateinit var bundle:Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentStoreBinding.inflate(inflater,container,false)
        viewModel= ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        viewModelVenueCategory=ViewModelProvider(requireActivity())[VenueCategoryViewModel::class.java]
        bundle=Bundle()
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
        viewModel.venueLiveData.observe(viewLifecycleOwner) {
            venueAdapter.venueList=it
            venueAdapter.viewType=VenueCategoryConstants.Store
            venueCategoryAdapter.venueList=it
            bundle.apply {
                putString(VenueCategoryConstants.VenueCategoryTitle,VenueCategoryConstants.VenueCategoryTitle)
                putSerializable(VenueConstants.venues, ArrayList(it))
            }
        }
    }
    private fun observeVenueCategories(){
        viewModelVenueCategory.storeCategoryLiveData.observe(viewLifecycleOwner) {
            venueCategoryAdapter.venueCategoryList=it
            venueCategoryAdapter.viewType= VenueCategoryConstants.Store
            bundle.apply {
                putString(VenueCategoryConstants.VenueCategoryTitle,VenueCategoryConstants.VenueCategoryTitle)
                putSerializable(VenueCategoryConstants.venueCategories, ArrayList(it))
                putString(VenueCategoryConstants.Venue, VenueCategoryConstants.Store)
            }
        }
    }
}