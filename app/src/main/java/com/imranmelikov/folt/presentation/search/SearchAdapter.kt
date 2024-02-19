package com.imranmelikov.folt.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.databinding.SearchRvBinding
import com.imranmelikov.folt.domain.model.Venue

class SearchAdapter:RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    class SearchViewHolder(val binding:SearchRvBinding):RecyclerView.ViewHolder(binding.root)

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding=SearchRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return venueList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val venue=venueList[position]
        holder.binding.ratingText.text=venue.venuePopularity.rating.toString()
        holder.binding.restaurantName.text=venue.venueName
        holder.binding.restaurantText.text=venue.venueText
        "${venue.delivery.deliveryPrice} Azn     ${venue.delivery.deliveryTime} min".also { holder.binding.priceText.text = it }

        setRatingIcon(venue,holder.binding.ratingIcon)

        val bundle= Bundle().apply {
            putSerializable(VenueConstants.venues,venue)
        }
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_searchFragment_to_venueDetailsFragment,bundle)
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
}