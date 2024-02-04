package com.imranmelikov.folt.presentation.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.databinding.FragmentCategoriesBinding
import com.imranmelikov.folt.domain.model.Offer
import com.imranmelikov.folt.domain.model.ParentVenue
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory
import com.imranmelikov.folt.presentation.discovery.OfferAdapter
import com.imranmelikov.folt.presentation.discovery.ParentVenueAdapter
import com.imranmelikov.folt.util.VenueConstants
import com.imranmelikov.folt.util.OfferConstants
import com.imranmelikov.folt.util.ParentVenueConstants
import com.imranmelikov.folt.util.VenueCategoryConstants
import com.imranmelikov.folt.util.ViewTypeDiscovery

@Suppress("DEPRECATION")
class CategoriesFragment : Fragment() {
   private lateinit var binding:FragmentCategoriesBinding
    private lateinit var venueCategoryAdapter: VenueCategoryAdapter
    private lateinit var parentVenueAdapter: ParentVenueAdapter
    private lateinit var offerAdapter: OfferAdapter
    private lateinit var offerList: List<Offer>
    private lateinit var venueList: List<Venue>
    private lateinit var venueCategoryList: List<VenueCategory>
    private lateinit var parentVenueList:List<ParentVenue>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCategoriesBinding.inflate(inflater,container,false)

        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        controlViewTypes()
        clickBackBtn()
    }
    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun controlViewTypes(){
        val getArguments=arguments?.getString(VenueCategoryConstants.VenueCategoryTitle)
        getArguments?.let {
            when (it) {
                ParentVenueConstants.ParentVenueTitle -> {
                    initialiseParentVenueRv()
                    getControlArgumentsParentVenue()
                }
                VenueCategoryConstants.VenueCategoryTitle -> {
                    initialiseVenueCategoryRv()
                    getControlArgumentsCategory()
                }
                OfferConstants.OfferTitle -> {
                    initialiseOfferRv()
                    getControlArgumentsOffer()
                }
            }
        }
    }
    private fun getControlArgumentsCategory(){
        val receiveArgs = arguments

        val receivedVenueString=receiveArgs?.getString(VenueCategoryConstants.Venue)
        if (receivedVenueString==VenueCategoryConstants.Restaurant){
            venueCategoryAdapter.viewTypeVenue=VenueCategoryConstants.Restaurant
        }else if (receivedVenueString==VenueCategoryConstants.Store){
            venueCategoryAdapter.viewTypeVenue=VenueCategoryConstants.Store
        }

        val receivedVenueList = receiveArgs?.getSerializable(VenueConstants.venues) as? ArrayList<*>
        venueList = receivedVenueList?.filterIsInstance<Venue>() ?: emptyList()
        venueCategoryAdapter.venueList=venueList

        val receivedVenueCategoryList = receiveArgs?.getSerializable(VenueCategoryConstants.venueCategories) as? ArrayList<*>
        venueCategoryList = receivedVenueCategoryList?.filterIsInstance<VenueCategory>() ?: emptyList()
        venueCategoryAdapter.venueCategoryList=venueCategoryList


        venueCategoryAdapter.viewType=VenueCategoryConstants.Category
    }
    private fun getControlArgumentsParentVenue(){
        val receiveArgs = arguments

        val receiveTitle=receiveArgs?.getString(ParentVenueConstants.titleString)
        receiveTitle?.let {
            binding.categoriesText.text=it
        }

        val receivedViewType=receiveArgs?.getInt(VenueCategoryConstants.Venue)
        if (receivedViewType==ViewTypeDiscovery.ParentRestaurant){
            parentVenueAdapter.viewType=ViewTypeDiscovery.ParentRestaurant
        }else if (receivedViewType==ViewTypeDiscovery.ParentStore){
            parentVenueAdapter.viewType=ViewTypeDiscovery.ParentStore
        }

        val receivedVenueList = receiveArgs?.getSerializable(VenueConstants.venues) as? ArrayList<*>
        venueList = receivedVenueList?.filterIsInstance<Venue>() ?: emptyList()
        parentVenueAdapter.venueList=venueList

        val receivedParentVenueList = receiveArgs?.getSerializable(ParentVenueConstants.parentVenues) as? ArrayList<*>
        parentVenueList = receivedParentVenueList?.filterIsInstance<ParentVenue>() ?: emptyList()
        parentVenueAdapter.parentVenueList=parentVenueList

        parentVenueAdapter.viewTypeFragment=ParentVenueConstants.Category
    }
    private fun getControlArgumentsOffer(){
        val receiveArgs = arguments

        val receiveTitle=receiveArgs?.getString(ParentVenueConstants.titleString)
        receiveTitle?.let {
            binding.categoriesText.text=it
        }

        val receivedViewType=receiveArgs?.getInt(VenueCategoryConstants.Venue)
        if (receivedViewType==ViewTypeDiscovery.ParentRestaurant){
            offerAdapter.viewType=ViewTypeDiscovery.ParentRestaurant
        }else if (receivedViewType==ViewTypeDiscovery.ParentStore){
            offerAdapter.viewType=ViewTypeDiscovery.ParentStore
        }

        val receivedVenueList = receiveArgs?.getSerializable(VenueConstants.venues) as? ArrayList<*>
        venueList = receivedVenueList?.filterIsInstance<Venue>() ?: emptyList()
        offerAdapter.venueList=venueList

        val receivedOfferList = receiveArgs?.getSerializable(OfferConstants.offer) as? ArrayList<*>
        offerList = receivedOfferList?.filterIsInstance<Offer>() ?: emptyList()
        offerAdapter.offerList=offerList

        offerAdapter.viewTypeFragment=ParentVenueConstants.Category
    }
    private fun initialiseVenueCategoryRv(){
        venueCategoryAdapter= VenueCategoryAdapter()
        binding.categoriesRv.layoutManager= GridLayoutManager(requireContext(),2)
        binding.categoriesRv.adapter=venueCategoryAdapter
    }
    private fun initialiseParentVenueRv(){
        parentVenueAdapter= ParentVenueAdapter()
        binding.categoriesRv.layoutManager=GridLayoutManager(requireContext(),2)
        binding.categoriesRv.adapter=parentVenueAdapter
    }
    private fun initialiseOfferRv(){
        offerAdapter= OfferAdapter()
        binding.categoriesRv.layoutManager=LinearLayoutManager(requireContext())
        binding.categoriesRv.adapter=offerAdapter
    }
}