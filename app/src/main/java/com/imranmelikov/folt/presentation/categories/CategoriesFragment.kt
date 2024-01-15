package com.imranmelikov.folt.presentation.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.imranmelikov.folt.databinding.FragmentCategoriesBinding
import com.imranmelikov.folt.presentation.restaurants.RestaurantViewModel
import com.imranmelikov.folt.presentation.stores.StoreViewModel
import com.imranmelikov.folt.util.ArgumentConstants
import com.imranmelikov.folt.util.VenueCategoryConstants

class CategoriesFragment : Fragment() {
   private lateinit var binding:FragmentCategoriesBinding
   private lateinit var viewModelVenueCategory:VenueCategoryViewModel
    private lateinit var viewModelRestaurant:RestaurantViewModel
    private lateinit var viewModelStores: StoreViewModel
    private lateinit var venueCategoryAdapter: VenueCategoryAdapter
    private var receiveArgumentString: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCategoriesBinding.inflate(inflater,container,false)
        viewModelRestaurant=ViewModelProvider(requireActivity())[RestaurantViewModel::class.java]
        viewModelStores= ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        viewModelVenueCategory=ViewModelProvider(requireActivity())[VenueCategoryViewModel::class.java]

        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        getControlArguments()
        initialiseVenueCategoryRv()
        clickBackBtn()
    }
    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun getControlArguments(){
        val receiveArgs = arguments
        receiveArgumentString = receiveArgs?.getString(ArgumentConstants.venues)

        when (receiveArgumentString) {
            VenueCategoryConstants.Restaurant -> {
                viewModelRestaurant.getVenues()
                viewModelVenueCategory.getRestaurantCategories()
                observeRestaurantCategories()
                observeRestaurants()
            }
            VenueCategoryConstants.Store -> {
                viewModelStores.getVenues()
                viewModelVenueCategory.getStoreCategories()
                observeStoreCategories()
                observeStores()
            }
        }
    }
    private fun initialiseVenueCategoryRv(){
        venueCategoryAdapter= VenueCategoryAdapter()
        binding.categoriesRv.layoutManager= GridLayoutManager(requireContext(),2)
        binding.categoriesRv.adapter=venueCategoryAdapter
    }
    private fun observeRestaurantCategories(){
        viewModelVenueCategory.restaurantCategoryLiveData.observe(viewLifecycleOwner) {
            venueCategoryAdapter.venueCategoryList = it
        }
    }
    private fun observeStoreCategories(){
        viewModelVenueCategory.storeCategoryLiveData.observe(viewLifecycleOwner){
            venueCategoryAdapter.venueCategoryList=it
        }
    }
    private fun observeRestaurants(){
        viewModelRestaurant.venueLiveData.observe(viewLifecycleOwner) {
            venueCategoryAdapter.venueList=it
            venueCategoryAdapter.viewType=VenueCategoryConstants.Category
        }
    }
    private fun observeStores(){
        viewModelStores.venueLiveData.observe(viewLifecycleOwner) {
            venueCategoryAdapter.venueList=it
            venueCategoryAdapter.viewType=VenueCategoryConstants.Category
        }
    }
}