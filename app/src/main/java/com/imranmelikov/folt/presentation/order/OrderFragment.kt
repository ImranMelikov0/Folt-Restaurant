package com.imranmelikov.folt.presentation.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.OrderConstants
import com.imranmelikov.folt.databinding.FragmentOrderBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.presentation.venuedetails.RestaurantMenuAdapter
import com.imranmelikov.folt.presentation.venuedetails.VenueDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class OrderFragment : Fragment() {
  private lateinit var binding:FragmentOrderBinding
  private lateinit var viewModel:VenueDetailsViewModel
  private lateinit var adapter:RestaurantMenuAdapter
    val bundle=Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[VenueDetailsViewModel::class.java]

        viewModel.getVenueDetailsFromRoom()
        observeVenueDetails()

        // click back button
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // get venue from venueDetailFragment and send to orderDetailFragment
        val receivedVenue = arguments?.getSerializable(OrderConstants.venueForOrder) as? Venue
        receivedVenue?.let {venue->
            binding.venueName.text=venue.venueName
            bundle.apply {
                putSerializable(OrderConstants.venueForOrder,venue)
            }
        }
        // click save button
        binding.saveBtn.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_orderDetailFragment,bundle)
        }
        return binding.root
    }


    private fun observeVenueDetails(){
        viewModel.venueDetailsLiveData.observe(viewLifecycleOwner){
            adapter= RestaurantMenuAdapter(requireActivity() as AppCompatActivity)
            binding.orderRv.layoutManager=LinearLayoutManager(requireContext())
            adapter.viewType=OrderConstants.orderVenueDetails
            adapter.restaurantMenuList=it
            binding.orderRv.adapter=adapter
        }
    }
}