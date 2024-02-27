package com.imranmelikov.folt.presentation.venue

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.VenuesRvBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.VenueCategoryConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VenueAdapter:RecyclerView.Adapter<VenueAdapter.VenueViewHolder>() {
    class VenueViewHolder(val binding:VenuesRvBinding):RecyclerView.ViewHolder(binding.root)

    lateinit var viewModel:VenueViewModel

    // viewType for Fragment
    var viewType:String=""

    //venueList for favorite
    var favVenueList= listOf<Venue>()


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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val binding=VenuesRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VenueViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return venueList.size
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val venueArraylist=venueList[position]
        holder.binding.restaurantName.text=venueArraylist.venueName
        holder.binding.restaurantText.text=venueArraylist.venueText
        "${venueArraylist.delivery.deliveryTime} min".also { holder.binding.venuesDeliveryTimeBtn.text = it }
        "${venueArraylist.delivery.deliveryPrice} Azn".also { holder.binding.deliveryText.text = it }
        holder.binding.ratingText.text=venueArraylist.venuePopularity.rating.toDouble().toString()
        Glide.with(holder.itemView.context)
            .load(venueArraylist.imageUrl)
            .into(holder.binding.restaurantImage)
        favIconView(venueArraylist,holder.binding.favIcon,holder.binding.progress,holder)
        setRatingIcon(venueArraylist,holder.binding.ratingIcon)
        setDeliveryTextColor(venueArraylist,holder)

        val bundle= Bundle().apply {
            putSerializable(VenueConstants.venues,venueArraylist)
        }
        holder.itemView.setOnClickListener {
            when(viewType){
                VenueCategoryConstants.Restaurant->{
                    Navigation.findNavController(it).navigate(R.id.action_restaurantFragment_to_venueDetailsFragment,bundle)
                }
                VenueCategoryConstants.Store->{
                    Navigation.findNavController(it).navigate(R.id.action_storeFragment_to_venueDetailsFragment,bundle)
                }
                VenueCategoryConstants.Venue->{
                    Navigation.findNavController(it).navigate(R.id.action_venueFragment_to_venueDetailsFragment,bundle)
                }
            }
        }
    }

    // favIcon view
    @SuppressLint("NotifyDataSetChanged")
    private fun favIconView(venueArraylist:Venue, favIcon: ImageView,progressBar: ProgressBar,holder: VenueViewHolder){
        if (favVenueList.isEmpty()){
            favIcon.setImageResource(R.drawable.heart_outline)
            favIcon.setOnClickListener {
                // Add venue to favorites
                progressBar.visibility=View.VISIBLE
                CoroutineScope(Dispatchers.Main).launch {
                    holder.itemView.setOnClickListener {}
                    favIcon.setOnClickListener {}
                    viewModel.insertFavoriteVenue(venueArraylist,"a")
                    delay(3000)
                    favIcon.setImageResource(R.drawable.heart_inline)
                    progressBar.visibility=View.GONE
                    favIconView(venueArraylist,favIcon,progressBar,holder)
                    notifyDataSetChanged()
                }
            }
        }else{
            favVenueList.map {
                if (venueArraylist.id==it.id){
                    favIcon.setImageResource(R.drawable.heart_inline)
                    favIcon.setOnClickListener {_->
                        // Remove venue from favorites
                        progressBar.visibility=View.VISIBLE
                        CoroutineScope(Dispatchers.Main).launch {
                            holder.itemView.setOnClickListener {}
                            favIcon.setOnClickListener {}
                            viewModel.deleteFavoriteVenue(it.id,"a")
                            delay(3000)
                            favIcon.setImageResource(R.drawable.heart_outline)
                            progressBar.visibility=View.GONE
                            favIconView(venueArraylist,favIcon,progressBar,holder)
                            notifyDataSetChanged()
                        }

                    }
                }else{
                    favIcon.setImageResource(R.drawable.heart_outline)
                    favIcon.setOnClickListener {
                        // Add venue to favorites
                        progressBar.visibility=View.VISIBLE
                        CoroutineScope(Dispatchers.Main).launch {
                            holder.itemView.setOnClickListener {}
                            favIcon.setOnClickListener {}
                            viewModel.insertFavoriteVenue(venueArraylist,"a")
                            delay(3000)
                            favIcon.setImageResource(R.drawable.heart_inline)
                            progressBar.visibility=View.GONE
                            favIconView(venueArraylist,favIcon,progressBar,holder)
                            notifyDataSetChanged()
                        }
                    }
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
    private fun setDeliveryTextColor(venueArraylist: Venue,holder: VenueViewHolder){
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