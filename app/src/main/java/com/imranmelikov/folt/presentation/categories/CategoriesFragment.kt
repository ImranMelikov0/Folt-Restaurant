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
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory
import com.imranmelikov.folt.presentation.restaurants.RestaurantViewModel
import com.imranmelikov.folt.presentation.stores.StoreViewModel
import com.imranmelikov.folt.util.ArgumentConstants
import com.imranmelikov.folt.util.VenueCategoryConstants

class CategoriesFragment : Fragment() {
   private lateinit var binding:FragmentCategoriesBinding
    private lateinit var venueCategoryAdapter: VenueCategoryAdapter
    private lateinit var venueList: List<Venue>
    private lateinit var venueCategoryList: List<VenueCategory>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCategoriesBinding.inflate(inflater,container,false)

        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        initialiseVenueCategoryRv()
        getControlArguments()
        clickBackBtn()
    }
    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun getControlArguments(){
        val receiveArgs = arguments

        val receivedVenueList = receiveArgs?.getSerializable(ArgumentConstants.venues) as? ArrayList<*>
        venueList = receivedVenueList?.filterIsInstance<Venue>() ?: emptyList()
        venueCategoryAdapter.venueList=venueList

        val receivedVenueCategoryList = receiveArgs?.getSerializable(ArgumentConstants.venueCategories) as? ArrayList<*>
        venueCategoryList = receivedVenueCategoryList?.filterIsInstance<VenueCategory>() ?: emptyList()
        venueCategoryAdapter.venueCategoryList=venueCategoryList

        venueCategoryAdapter.viewType=VenueCategoryConstants.Category
    }
    private fun initialiseVenueCategoryRv(){
        venueCategoryAdapter= VenueCategoryAdapter()
        binding.categoriesRv.layoutManager= GridLayoutManager(requireContext(),2)
        binding.categoriesRv.adapter=venueCategoryAdapter
    }
}