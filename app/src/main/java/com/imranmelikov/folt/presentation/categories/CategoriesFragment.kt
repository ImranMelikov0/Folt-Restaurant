package com.imranmelikov.folt.presentation.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.imranmelikov.folt.databinding.FragmentCategoriesBinding
import com.imranmelikov.folt.presentation.restaurants.RestaurantViewModel
import com.imranmelikov.folt.util.VenueCategoryConstants

class CategoriesFragment : Fragment() {
   private lateinit var binding:FragmentCategoriesBinding
   private lateinit var viewModelVenueCategory:VenueCategoryViewModel
    private lateinit var viewModelRestaurant:RestaurantViewModel
    private lateinit var venueCategoryAdapter: VenueCategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCategoriesBinding.inflate(inflater,container,false)
        viewModelRestaurant=ViewModelProvider(requireActivity())[RestaurantViewModel::class.java]
        viewModelVenueCategory=ViewModelProvider(requireActivity())[VenueCategoryViewModel::class.java]
        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        viewModelRestaurant.getVenues()
        viewModelVenueCategory.getVenueCategories()
        initialiseVenueCategoryRv()
        observeVenueCategories()
        observeVenues()
        clickBackBtn()
    }
    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun initialiseVenueCategoryRv(){
        venueCategoryAdapter= VenueCategoryAdapter()
        binding.categoriesRv.layoutManager= GridLayoutManager(requireContext(),2)
        binding.categoriesRv.adapter=venueCategoryAdapter
    }
    private fun observeVenueCategories(){
        viewModelVenueCategory.venueCategoryLiveData.observe(viewLifecycleOwner, Observer {
            venueCategoryAdapter.venueCategoryList=it
        })
    }
    private fun observeVenues(){
        viewModelRestaurant.venueLiveData.observe(viewLifecycleOwner, Observer {
            venueCategoryAdapter.venueList=it
            venueCategoryAdapter.viewType=VenueCategoryConstants.Category
        })
    }
}