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
import com.imranmelikov.folt.util.ArgumentConstants
import com.imranmelikov.folt.util.VenueCategoryConstants

class RestaurantFragment : Fragment() {
    private lateinit var binding:FragmentRestaurantBinding
    private lateinit var viewModelRestaurant:RestaurantViewModel
    private lateinit var viewModelVenueCategory:VenueCategoryViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var venueCategoryAdapter: VenueCategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRestaurantBinding.inflate(inflater,container,false)
        viewModelRestaurant=ViewModelProvider(requireActivity())[RestaurantViewModel::class.java]
        viewModelVenueCategory=ViewModelProvider(requireActivity())[VenueCategoryViewModel::class.java]

        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        viewModelRestaurant.getVenues()
        viewModelVenueCategory.getRestaurantCategories()
        initialiseRestaurantRv()
        initialiseVenueCategoryRv()
        observeVenueCategories()
        observeVenues()
        clickSeeAllBtn()
    }
  private fun clickSeeAllBtn(){
        binding.seeAllBtn.setOnClickListener {
            val data = VenueCategoryConstants.Restaurant
            val bundle = Bundle()
            bundle.putString(ArgumentConstants.venues, data)
            findNavController().navigate(R.id.action_restaurantFragment_to_categoriesFragment,bundle)
        }
    }
    private fun initialiseVenueCategoryRv(){
        venueCategoryAdapter= VenueCategoryAdapter()
        binding.categoryRv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.categoryRv.adapter=venueCategoryAdapter
    }

    private fun initialiseRestaurantRv(){
        restaurantAdapter= RestaurantAdapter()
        binding.restaurantRv.layoutManager=LinearLayoutManager(requireContext())
        binding.restaurantRv.adapter=restaurantAdapter
    }

    private fun observeVenues(){
        viewModelRestaurant.venueLiveData.observe(viewLifecycleOwner) {
            restaurantAdapter.venueList=it
            venueCategoryAdapter.venueList=it
        }
    }
    private fun observeVenueCategories(){
        viewModelVenueCategory.restaurantCategoryLiveData.observe(viewLifecycleOwner) {
            venueCategoryAdapter.venueCategoryList=it
            venueCategoryAdapter.viewType=VenueCategoryConstants.Restaurant
        }
    }
}