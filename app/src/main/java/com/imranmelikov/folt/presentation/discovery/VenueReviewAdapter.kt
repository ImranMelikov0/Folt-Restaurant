package com.imranmelikov.folt.presentation.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.RestaurantReviewRvBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.ViewTypeDiscovery

class VenueReviewAdapter:RecyclerView.Adapter<VenueReviewAdapter.VenueReviewViewHolder>() {
    class VenueReviewViewHolder(val binding:RestaurantReviewRvBinding):RecyclerView.ViewHolder(binding.root)

    // viewType for Venue
    var viewType:Int=-45

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueReviewViewHolder {
        val binding=RestaurantReviewRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VenueReviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return venueList.size
    }

    override fun onBindViewHolder(holder: VenueReviewViewHolder, position: Int) {
        val venue=venueList[position]
        holder.binding.ratingText.text=venue.venuePopularity.rating.toString()
        "${venue.delivery.deliveryPrice} Azn".also { holder.binding.deliveryText.text = it }
        holder.binding.restaurantName.text=venue.venueName
        "${venue.delivery.deliveryTime} min".also { holder.binding.deliveryTimeText.text = it }
        holder.binding.restaurantText.text=venue.venueText

        Glide.with(holder.itemView.context)
            .load(venue.imageUrl)
            .into(holder.binding.restaurantImage)

        setRatingIcon(venue,holder.binding.ratingIcon)
        setDeliveryTextColor(venue,holder)
        val bundle=Bundle()
        bundle.apply {
            putSerializable(VenueConstants.venues,venue)
        }
        holder.itemView.setOnClickListener {
            when(viewType){
                ViewTypeDiscovery.Venue->{
                    Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_venueDetailsFragment,bundle)
                }
                ViewTypeDiscovery.Profile->{
                    Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_venueDetailsFragment,bundle)
                }
            }
        }
    }

    // set RatingIcon
    private fun setRatingIcon(venueArraylist: Venue,ratingIcon: ImageView){
        when {
            // Rating less than 5,00
            venueArraylist.venuePopularity.rating.toDouble() < 5.00 ->{
                ratingIcon.setImageResource(R.drawable.emoticon_sad)
            }
            venueArraylist.venuePopularity.rating.toDouble() <9.00 -> {
                //Rating less than 9.00
                ratingIcon.setImageResource(R.drawable.emoticon_happy)
            }
            else ->{
                //Rating more than 9.00
                ratingIcon.setImageResource(R.drawable.emoticon_very_happy)
            }
        }
    }
    // set deliveryTextColor
    private fun setDeliveryTextColor(venueArraylist: Venue,holder: VenueReviewViewHolder){
        // DeliveryPrice more than 0,00
        if (venueArraylist.delivery.deliveryPrice.toDouble()>0.00){
            holder.binding.deliveryText.setTextColor(
                ContextCompat.getColor(holder.itemView.context,
                    R.color.black))
        }else{
            // DeliveryPrice equal 0,00
            holder.binding.deliveryText.setTextColor(
                ContextCompat.getColor(holder.itemView.context,
                    R.color.open_blue))
        }
    }
}