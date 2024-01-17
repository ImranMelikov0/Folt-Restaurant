package com.imranmelikov.folt.presentation.venuedetails

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentVenueDetailsBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.util.ArgumentConstants

@Suppress("DEPRECATION")
class VenueDetailsFragment : Fragment() {
    private lateinit var binding:FragmentVenueDetailsBinding
     val bundle=Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentVenueDetailsBinding.inflate(inflater,container,false)
        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        getControlArguments()
        (activity as MainActivity).hideBottomNav()
    }

    private fun clickBtn(venue: Venue){
        clickBackBtn()
        clickDeliveryBtn()
        clickFavBtn(venue)
        clickMoreBtn(venue)
        clickShareBtn(venue)
        clickSearchBtn(venue)
    }
    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
            (activity as MainActivity).showBottomNav()
        }
    }
    private fun clickDeliveryBtn(){
        // !!!!!!
        binding.deliveryBtn.setOnClickListener {
        }
    }
    private fun clickFavBtn(venue: Venue){
        //!!!!!!!
        if (venue.venuePopularity.favorite){
            binding.favImg.setImageResource(R.drawable.heart_inline)
            binding.favBtn.setOnClickListener {
                binding.favImg.setImageResource(R.drawable.heart_outline)
               venue.venuePopularity.favorite=false
                clickFavBtn(venue)
            }
        }else{
            binding.favImg.setImageResource(R.drawable.heart_outline)
            binding.favBtn.setOnClickListener {
                binding.favImg.setImageResource(R.drawable.heart_inline)
                venue.venuePopularity.favorite=true
                clickFavBtn(venue)
            }
        }
    }
    private fun clickSearchBtn(venue: Venue){
        binding.searchBtn.setOnClickListener {
            bundle.apply {
                putSerializable(ArgumentConstants.venues,venue)
            }
            findNavController().navigate(R.id.action_venueDetailsFragment_to_itemSearchFragment)
        }
    }
    private fun clickMoreBtn(venue: Venue){
        binding.moreTextview.setOnClickListener {
           bundle.apply {
                putSerializable(ArgumentConstants.venueInformation,venue)
            }
            findNavController().navigate(R.id.action_venueDetailsFragment_to_venueInformationFragment,bundle)
        }
    }
    private fun clickShareBtn(venue: Venue){
        binding.shareBtn.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, venue.venueName +" "+ venue.venueInformation.url)
            }
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }
    }

    private fun getControlArguments(){
        val receiveArgs = arguments

        val receivedVenue = receiveArgs?.getSerializable(ArgumentConstants.venues) as? Venue
        receivedVenue?.let {venue->
            "Delivery : ${venue.delivery.deliveryPrice} AZN".also { binding.deliveryTextview.text = it }
            binding.venueNameTextview.text=venue.venueName
            "Rating : ${venue.venuePopularity.rating}".also { binding.ratingTextview.text=it }
            "Delivery ${venue.delivery.deliveryTime} min    ▾".also { binding.deliveryBtn.text=it }
            Glide.with(requireActivity())
                .load(venue.image)
                .into(binding.logo)
            Glide.with(requireActivity())
                .load(venue.image)
                .into(binding.mainImage)

            clickBtn(venue)
        }
    }
}