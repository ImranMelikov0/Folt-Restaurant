package com.imranmelikov.folt.presentation.orderdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.constants.OrderConstants
import com.imranmelikov.folt.databinding.FragmentOrderDetailBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.presentation.venuedetails.VenueDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class OrderDetailFragment : Fragment() {
  private lateinit var binding:FragmentOrderDetailBinding
  private lateinit var viewModel: VenueDetailsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentOrderDetailBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[VenueDetailsViewModel::class.java]

        viewModel.getVenueDetailsFromRoom()

       observeUser()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun clickCompleteBtn(){
        binding.saveBtn.setOnClickListener {

        }
    }

    private fun observeVenueDetails(){
        val receivedVenue = arguments?.getSerializable(OrderConstants.venueForOrder) as? Venue
        receivedVenue?.let { venue->
            var itemSubtotalPrice=0.0
            viewModel.venueDetailsLiveData.observe(viewLifecycleOwner){venueDetailList->
                venueDetailList.map { venueDetail->
                    itemSubtotalPrice += venueDetail.price.toDouble()
                    binding.servicePriceText.text=venue.serviceFee.toString()
                    binding.itemSubtotalPriceText.text=itemSubtotalPrice.toString()
                    binding.deliveryPriceText.text=venue.delivery.deliveryPrice
                    (itemSubtotalPrice + venue.delivery.deliveryPrice.toDouble()
                            + venue.serviceFee.toDouble()).toString().also { binding.totalPriceText.text = it }
                    "${venue.delivery.deliveryTime} min".also { binding.deliveryTimeText.text = it }
                    binding.restaurantName.text=venue.venueName
                }
            }
        }
    }
    private fun observeAddress(){
        observeVenueDetails()
    }
    private fun observeUser(){
        observeAddress()
    }
}