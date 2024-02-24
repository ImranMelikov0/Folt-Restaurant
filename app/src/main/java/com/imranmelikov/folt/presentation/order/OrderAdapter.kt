package com.imranmelikov.folt.presentation.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imranmelikov.folt.data.local.entity.VenueDetailsRoom
import com.imranmelikov.folt.databinding.RestaurantMenuRvBinding

class OrderAdapter:RecyclerView.Adapter<OrderAdapter.RestaurantMenuViewHolder>() {
    class RestaurantMenuViewHolder(val binding:RestaurantMenuRvBinding):RecyclerView.ViewHolder(binding.root)

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<VenueDetailsRoom>(){
        override fun areItemsTheSame(oldItem: VenueDetailsRoom, newItem: VenueDetailsRoom): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: VenueDetailsRoom, newItem: VenueDetailsRoom): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of MenuList
    var venueMenuList:List<VenueDetailsRoom>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuViewHolder {
        val binding=RestaurantMenuRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RestaurantMenuViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return venueMenuList.size
    }

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, position: Int) {
        val menuList=venueMenuList[position]
        holder.binding.restaurantText.text=menuList.about
        holder.binding.restaurantName.text=menuList.menuName
        val formattedResult = String.format("%.2f", menuList.price)
        holder.binding.priceText.text=formattedResult
        Glide.with(holder.itemView.context)
            .load(menuList.imageUrl)
            .into(holder.binding.restaurantImage)
    }
}