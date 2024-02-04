package com.imranmelikov.folt.presentation.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.OfferRvBinding
import com.imranmelikov.folt.domain.model.Offer
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.util.VenueConstants
import com.imranmelikov.folt.util.ParentVenueConstants
import com.imranmelikov.folt.util.VenueCategoryConstants
import com.imranmelikov.folt.util.ViewTypeDiscovery

class OfferAdapter:RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {
    class OfferViewHolder(val binding:OfferRvBinding):RecyclerView.ViewHolder(binding.root)

    var venueList= listOf<Venue>()

    //viewType for venue
    var viewType:Int=-45

    //viewType for fragment
    var viewTypeFragment=""

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<Offer>(){
        override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of offer
    var offerList:List<Offer>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding=OfferRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OfferViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer=offerList[position]

        Glide.with(holder.itemView.context)
            .load(offer.banner.image)
            .into(holder.binding.offerImage)

        val filteredVenueList=venueList.filter { it.venueName==offer.parentVenue }
        val bundleRestaurant = Bundle().apply {
            putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
            putString(VenueCategoryConstants.DiscoveryTitle,offer.banner.title)
            putString(VenueCategoryConstants.Venue, VenueCategoryConstants.Restaurant)
        }
        val bundleStore = Bundle().apply {
            putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
            putString(VenueCategoryConstants.DiscoveryTitle,offer.banner.title)
            putString(VenueCategoryConstants.Venue, VenueCategoryConstants.Store)
        }
        holder.itemView.setOnClickListener {
            when(viewTypeFragment){
                ParentVenueConstants.Discovery->{
                    when(viewType){
                        ViewTypeDiscovery.OfferStore->{
                            Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_venueFragment,bundleStore)
                        }
                        ViewTypeDiscovery.OfferRestaurant->{
                            Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_venueFragment,bundleRestaurant)
                        }
                    }
                }
                ParentVenueConstants.Category->{
                    when(viewType){
                        ViewTypeDiscovery.OfferStore->{
                            Navigation.findNavController(it).navigate(R.id.action_categoriesFragment_to_venueFragment,bundleStore)
                        }
                        ViewTypeDiscovery.OfferRestaurant->{
                            Navigation.findNavController(it).navigate(R.id.action_categoriesFragment_to_venueFragment,bundleRestaurant)
                        }
                    }
                }
            }

        }
    }
}