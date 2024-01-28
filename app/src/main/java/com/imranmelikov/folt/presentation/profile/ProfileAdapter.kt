package com.imranmelikov.folt.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.databinding.RestaurantReviewRvBinding
import com.imranmelikov.folt.domain.model.Venue

class ProfileAdapter:RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
    class ProfileViewHolder(val binding:RestaurantReviewRvBinding):RecyclerView.ViewHolder(binding.root)

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<Venue>(){
        override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of VenueList
    var venueList:List<Venue>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding=RestaurantReviewRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return venueList.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val venue=venueList[position]
        holder.binding.ratingText.text=venue.venuePopularity.rating.toString()
        holder.binding.deliveryText.text=venue.delivery.deliveryPrice
        holder.binding.restaurantName.text=venue.venueName
        holder.binding.deliveryTimeText.text=venue.delivery.deliveryTime
        holder.binding.restaurantText.text=venue.venueText
    }
}