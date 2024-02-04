package com.imranmelikov.folt.presentation.venueinformation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.databinding.FragmentVenueInformationBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.util.VenueInformationConstants

@Suppress("DEPRECATION")
class VenueInformationFragment : Fragment() {
    private lateinit var binding:FragmentVenueInformationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentVenueInformationBinding.inflate(inflater,container,false)
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        getControlArguments()
        return binding.root
    }

    private fun getControlArguments(){
        val receivedVenueList = arguments?.getSerializable(VenueInformationConstants.venueInformation) as? Venue
        receivedVenueList?.let {
            binding.addressText.text=it.venueInformation.address
            binding.tel.text=it.venueInformation.tel
            binding.deliveryTime.text=it.delivery.deliveryTime
            binding.restaurantAboutText.text=it.venueText
            binding.restaurantTextview.text=it.venueName
            binding.toolbarVenueName.text=it.venueName
            binding.deliveryAmount.text=it.delivery.deliveryPrice
            binding.openSite.text=it.venueInformation.url
            if (it.venueInformation.isOpen){
                binding.openingBoolean.text=VenueInformationConstants.open
            }else{
                binding.openingBoolean.text=VenueInformationConstants.close
            }

            binding.tel.setOnClickListener { _ ->
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${it.venueInformation.tel}")
                startActivity(intent)
            }
        }

    }
}