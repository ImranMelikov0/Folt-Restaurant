package com.imranmelikov.folt.presentation.restaurants

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.databinding.FragmentRestaurantBinding
import com.imranmelikov.folt.presentation.categories.VenueCategoryAdapter
import com.imranmelikov.folt.presentation.categories.VenueCategoryViewModel
import com.imranmelikov.folt.presentation.venue.VenueAdapter
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.VenueCategoryConstants
import com.imranmelikov.folt.presentation.venue.VenueViewModel
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        observeFavVenues()
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
        venueAdapter.viewModel=viewModelVenue
        binding.restaurantRv.layoutManager=LinearLayoutManager(requireContext())
        binding.restaurantRv.adapter=venueAdapter
    }

    private fun observeVenues(){
        viewModelVenue.venueLiveData.observe(viewLifecycleOwner) {result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {venueList->
                        val filteredVenueList=venueList.filter { it.restaurant }
                        venueAdapter.venueList=filteredVenueList
                        venueAdapter.viewType=VenueCategoryConstants.Restaurant
                        venueCategoryAdapter.venueList=filteredVenueList
                        bundle.apply {
                            putString(VenueCategoryConstants.VenueCategoryTitle,VenueCategoryConstants.VenueCategoryTitle)
                            putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
                        }
                    }
                    binding.restaurantProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.GONE
                }
                Status.LOADING->{
                    binding.restaurantProgress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.restaurantProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
            }
        }
    }
    private fun observeFavVenues(){
        viewModelVenue.favoriteVenueLiveData.observe(viewLifecycleOwner) {result->
            when(result.status){
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.restaurantProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
                Status.SUCCESS->{
                    result.data?.let {venues ->
                        venueAdapter.favVenueList=venues
                    }
                    binding.restaurantProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.GONE
                }
                Status.LOADING->{
                    binding.restaurantProgress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
            }

        }

    }
    private fun observeVenueCategories(){
        viewModelVenueCategory.venueCategoryLiveData.observe(viewLifecycleOwner) {result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {venueCategoryList->
                        val filteredCategoryList=venueCategoryList.filter { it.restaurant }
                        venueCategoryAdapter.venueCategoryList=filteredCategoryList
                        venueCategoryAdapter.viewType=VenueCategoryConstants.Restaurant
                        bundle.apply {
                            putString(VenueCategoryConstants.VenueCategoryTitle,VenueCategoryConstants.VenueCategoryTitle)
                            putSerializable(VenueCategoryConstants.venueCategories, ArrayList(filteredCategoryList))
                        }
                    }
                    binding.restaurantProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.GONE
                }
                Status.LOADING->{
                    binding.restaurantProgress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.restaurantProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
            }
        }
    }
}