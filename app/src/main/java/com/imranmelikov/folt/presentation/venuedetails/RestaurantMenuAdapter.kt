package com.imranmelikov.folt.presentation.venuedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imranmelikov.folt.databinding.RestaurantMenuRvBinding
import com.imranmelikov.folt.domain.model.VenueDetails
import com.imranmelikov.folt.presentation.bottomsheetfragments.MenuBottomSheetFragment
import javax.inject.Inject

class RestaurantMenuAdapter @Inject constructor(private val context:AppCompatActivity):RecyclerView.Adapter<RestaurantMenuAdapter.RestaurantMenuViewHolder>() {
    class RestaurantMenuViewHolder(val binding:RestaurantMenuRvBinding):RecyclerView.ViewHolder(binding.root)

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<VenueDetails>(){
        override fun areItemsTheSame(oldItem: VenueDetails, newItem: VenueDetails): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: VenueDetails, newItem: VenueDetails): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of MenuList
    var restaurantMenuList:List<VenueDetails>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuViewHolder {
        val binding=RestaurantMenuRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RestaurantMenuViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return restaurantMenuList.size
    }

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, position: Int) {
        val menuList=restaurantMenuList[position]
        holder.binding.restaurantText.text=menuList.about
        holder.binding.restaurantName.text=menuList.menuName
        holder.binding.priceText.text=menuList.price.toString()
        Glide.with(holder.itemView.context)
            .load(menuList.imageUrl)
            .into(holder.binding.restaurantImage)
        val bottomSheetFragment = MenuBottomSheetFragment()
        holder.itemView.setOnClickListener {
                bottomSheetFragment.show((context).supportFragmentManager, bottomSheetFragment.tag)
                bottomSheetFragment.venueDetails=menuList
        }
        bottomSheetFragment.onItemClick={
            menuList.selected=true
        }

    }
}