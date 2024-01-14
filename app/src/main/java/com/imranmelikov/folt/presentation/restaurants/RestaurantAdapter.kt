package com.imranmelikov.folt.presentation.restaurants

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.VenuesRvBinding
import com.imranmelikov.folt.domain.model.Venue

class RestaurantAdapter():RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {
    class RestaurantViewHolder(val binding:VenuesRvBinding):RecyclerView.ViewHolder(binding.root)

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

    // Getter and setter for the list of Notes
    var venueList:List<Venue>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding=VenuesRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RestaurantViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return venueList.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val venueArraylist=venueList[position]
        holder.binding.restaurantName.text=venueArraylist.venueName
        holder.binding.restaurantText.text=venueArraylist.venueText
        holder.binding.venuesDeliveryTimeBtn.text=venueArraylist.delivery.deliveryTime
        "${venueArraylist.delivery.deliveryPrice} Azn".also { holder.binding.deliveryText.text = it }
        holder.binding.ratingText.text=venueArraylist.venuePopularity.rating.toString()

        clickFavIcon(venueArraylist,holder.binding.favIcon)
        setRatingIcon(venueArraylist,holder.binding.ratingIcon)
        setDeliveryTextColor(venueArraylist,holder)

        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_restaurantFragment_to_venueDetailsFragment)
        }
    }

    // click favIcon
   private fun clickFavIcon(venueArraylist:Venue,favIcon:ImageView){
        favIcon.setOnClickListener {
            // Remove venue from favorites
            if (venueArraylist.venuePopularity.favorite){
                favIcon.setImageResource(R.drawable.heart_outline)
                venueArraylist.venuePopularity.favorite=false
            }else{
                // Add venue to favorites
                favIcon.setImageResource(R.drawable.heart_inline)
                venueArraylist.venuePopularity.favorite=true
            }
        }
    }
    // set RatingIcon
    private fun setRatingIcon(venueArraylist: Venue,ratingIcon:ImageView){
        when {
            // Rating less than 5,00
            venueArraylist.venuePopularity.rating < 5.00 ->{
                ratingIcon.setImageResource(R.drawable.emoticon_sad)
            }
            venueArraylist.venuePopularity.rating <9.00 -> {
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
    private fun setDeliveryTextColor(venueArraylist: Venue,holder:RestaurantViewHolder){
        // DeliveryPrice more than 0,00
        if (venueArraylist.delivery.deliveryPrice.toDouble()>0.00){
            holder.binding.deliveryText.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.black))
        }else{
            // DeliveryPrice equal 0,00
            holder.binding.deliveryText.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.open_blue))
        }
    }
}