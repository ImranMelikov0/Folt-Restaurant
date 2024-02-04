package com.imranmelikov.folt.presentation.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.DiscoveryParentVenueBinding
import com.imranmelikov.folt.domain.model.ParentVenue
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.util.VenueConstants
import com.imranmelikov.folt.util.ParentVenueConstants
import com.imranmelikov.folt.util.VenueCategoryConstants
import com.imranmelikov.folt.util.ViewTypeDiscovery

class ParentVenueAdapter:RecyclerView.Adapter<ParentVenueAdapter.ParentVenueViewHolder>() {
    class ParentVenueViewHolder(val binding:DiscoveryParentVenueBinding):RecyclerView.ViewHolder(binding.root)

    //viewType for venue
    var viewType:Int=-45

    //viewType for fragment
    var viewTypeFragment=""

    var venueList= listOf<Venue>()

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<ParentVenue>(){
        override fun areItemsTheSame(oldItem: ParentVenue, newItem: ParentVenue): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ParentVenue, newItem: ParentVenue): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of parentVenue
    var parentVenueList:List<ParentVenue>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentVenueViewHolder {
        val binding=DiscoveryParentVenueBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ParentVenueViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return parentVenueList.size
    }

    override fun onBindViewHolder(holder: ParentVenueViewHolder, position: Int) {
        val parentVenue=parentVenueList[position]
        holder.binding.parentVenueName.text=parentVenue.venueName

        val filteredVenueList=venueList.filter { it.parentVenueId==parentVenue.id }
        val bundleRestaurant = Bundle().apply {
            putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
            putString(VenueCategoryConstants.DiscoveryTitle,parentVenue.venueName)
            putString(VenueCategoryConstants.Venue, VenueCategoryConstants.Restaurant)
        }
        val bundleStore = Bundle().apply {
            putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
            putString(VenueCategoryConstants.DiscoveryTitle,parentVenue.venueName)
            putString(VenueCategoryConstants.Venue, VenueCategoryConstants.Store)
        }
        holder.itemView.setOnClickListener {
            when(viewTypeFragment){
                ParentVenueConstants.Discovery->{
                    when(viewType){
                        ViewTypeDiscovery.ParentStore->{
                            Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_venueFragment,bundleStore)
                        }
                        ViewTypeDiscovery.ParentRestaurant->{
                            Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_venueFragment,bundleRestaurant)
                        }
                    }
                }
                ParentVenueConstants.Category->{
                    when(viewType){
                        ViewTypeDiscovery.ParentStore->{
                            Navigation.findNavController(it).navigate(R.id.action_categoriesFragment_to_venueFragment,bundleStore)
                        }
                        ViewTypeDiscovery.ParentRestaurant->{
                            Navigation.findNavController(it).navigate(R.id.action_categoriesFragment_to_venueFragment,bundleRestaurant)
                        }
                    }
                }
            }

        }
    }
}