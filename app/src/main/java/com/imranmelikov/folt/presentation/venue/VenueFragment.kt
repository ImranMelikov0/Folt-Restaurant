package com.imranmelikov.folt.presentation.venue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.databinding.FragmentVenueBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory
import com.imranmelikov.folt.util.VenueConstants
import com.imranmelikov.folt.util.VenueCategoryConstants

@Suppress("DEPRECATION")
class VenueFragment : Fragment() {
  private lateinit var binding:FragmentVenueBinding
  private lateinit var venueAdapter: VenueAdapter
  private lateinit var venueList: List<Venue>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentVenueBinding.inflate(inflater,container,false)
        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        initialiseVenueRv()
        getControlArguments()
        clickBackBtn()
    }

    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getControlArguments(){
        val receiveVenueString=arguments?.getString(VenueCategoryConstants.Venue)

        if (receiveVenueString==VenueCategoryConstants.Restaurant){
            venueAdapter.viewTypeVenue=VenueCategoryConstants.Restaurant
        }else if (receiveVenueString==VenueCategoryConstants.Store){
            venueAdapter.viewTypeVenue=VenueCategoryConstants.Store
        }

        val receivedVenueList = arguments?.getSerializable(VenueConstants.venues) as? ArrayList<*>
        venueList = receivedVenueList?.filterIsInstance<Venue>() ?: emptyList()
        venueAdapter.venueList=venueList
       venueAdapter.viewType=VenueCategoryConstants.Venue

        val receiveDiscoveryTitleString=arguments?.getString(VenueCategoryConstants.DiscoveryTitle)

        if (receiveDiscoveryTitleString!=null){
            binding.fastestDelivery.text=receiveDiscoveryTitleString
            binding.toolbarVenueText.text=receiveDiscoveryTitleString
        }

        val receivedVenueCategory = arguments?.getSerializable(VenueCategoryConstants.venueCategories) as? VenueCategory

        if (receivedVenueCategory!=null){
                binding.fastestDelivery.text=receivedVenueCategory.title
                binding.toolbarVenueText.text=receivedVenueCategory.title
        }
    }
    private fun initialiseVenueRv(){
        venueAdapter= VenueAdapter()
        binding.venuesRv.layoutManager=LinearLayoutManager(requireContext())
        binding.venuesRv.adapter=venueAdapter
    }
}