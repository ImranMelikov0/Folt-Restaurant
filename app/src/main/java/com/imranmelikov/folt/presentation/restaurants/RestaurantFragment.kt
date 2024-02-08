package com.imranmelikov.folt.presentation.restaurants

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentRestaurantBinding
import com.imranmelikov.folt.presentation.categories.VenueCategoryAdapter
import com.imranmelikov.folt.presentation.categories.VenueCategoryViewModel
import com.imranmelikov.folt.presentation.venue.VenueAdapter
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.VenueCategoryConstants
import com.imranmelikov.folt.presentation.venue.VenueViewModel

class RestaurantFragment : Fragment() {
    private lateinit var binding:FragmentRestaurantBinding
    private lateinit var viewModelVenue:VenueViewModel
    private lateinit var viewModelVenueCategory:VenueCategoryViewModel
    private lateinit var venueAdapter: VenueAdapter
    private lateinit var venueCategoryAdapter: VenueCategoryAdapter
    private lateinit var bundle:Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRestaurantBinding.inflate(inflater,container,false)
        viewModelVenue=ViewModelProvider(requireActivity())[VenueViewModel::class.java]
        viewModelVenueCategory=ViewModelProvider(requireActivity())[VenueCategoryViewModel::class.java]
        bundle=Bundle()
        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        viewModelVenue.getVenues()
        viewModelVenueCategory.getVenueCategories()
        initialiseRestaurantRv()
        initialiseVenueCategoryRv()
        observeVenueCategories()
        observeVenues()
        clickSeeAllBtn()
    }
  private fun clickSeeAllBtn(){
        binding.seeAllBtn.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFragment_to_categoriesFragment,bundle)
        }
    }
    private fun initialiseVenueCategoryRv(){
        venueCategoryAdapter= VenueCategoryAdapter()
        binding.categoryRv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.categoryRv.adapter=venueCategoryAdapter
    }

    private fun initialiseRestaurantRv(){
        venueAdapter= VenueAdapter()
        binding.restaurantRv.layoutManager=LinearLayoutManager(requireContext())
        binding.restaurantRv.adapter=venueAdapter
    }

    private fun observeVenues(){
        viewModelVenue.venueLiveData.observe(viewLifecycleOwner) {venueList->
            val filteredVenueList=venueList.filter { it.restaurant }
            venueAdapter.venueList=filteredVenueList
            venueAdapter.viewType=VenueCategoryConstants.Restaurant
            venueCategoryAdapter.venueList=filteredVenueList
            bundle.apply{
                putString(VenueCategoryConstants.VenueCategoryTitle,VenueCategoryConstants.VenueCategoryTitle)
                putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
            }
        }
    }
    private fun observeVenueCategories(){
        viewModelVenueCategory.venueCategoryLiveData.observe(viewLifecycleOwner) {venueCategoryList->
            val filteredCategoryList=venueCategoryList.filter { it.restaurant }
            venueCategoryAdapter.venueCategoryList=filteredCategoryList
            venueCategoryAdapter.viewType=VenueCategoryConstants.Restaurant
            bundle.apply {
                putString(VenueCategoryConstants.VenueCategoryTitle,VenueCategoryConstants.VenueCategoryTitle)
                putSerializable(VenueCategoryConstants.venueCategories, ArrayList(filteredCategoryList))
            }
        }
    }
}