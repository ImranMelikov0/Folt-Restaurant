package com.imranmelikov.folt.presentation.venuedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.constants.ItemSearchConstants
import com.imranmelikov.folt.databinding.VenueDetailsCategoryRvBinding
import com.imranmelikov.folt.domain.model.VenueDetailsItem
import com.imranmelikov.folt.constants.VenueMenuConstants
import javax.inject.Inject

class VenueDetailsAdapter @Inject constructor(private val context:AppCompatActivity):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewType=-45

    inner class VenueDetailsCategoryViewHolder(val binding:VenueDetailsCategoryRvBinding):RecyclerView.ViewHolder(binding.root){
        fun bindRestaurantMenu(venueDetailsItem: VenueDetailsItem){
                binding.categoryName.text=venueDetailsItem.title
             // initialize restaurantRv
                binding.restaurantRv.layoutManager=LinearLayoutManager(binding.root.context)
                val adapter=RestaurantMenuAdapter(context)
            venueDetailsItem.venueDetailList?.let {
                adapter.restaurantMenuList=it
                adapter.viewType=VenueMenuConstants.RestaurantMenu
                binding.restaurantRv.adapter=adapter
            }
        }
        fun bindStoreMenuCategory(venueDetailsItem: VenueDetailsItem){
            binding.categoryName.text=venueDetailsItem.title
         //initialize restaurantRv
            binding.restaurantRv.layoutManager=GridLayoutManager(binding.root.context,2)
            val adapter=StoreMenuCategoryAdp()
            venueDetailsItem.storeMenuCategory?.let {
                adapter.storeMenuCategoryList=it
                adapter.viewType=viewType
                binding.restaurantRv.adapter=adapter
            }
        }
    }

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<VenueDetailsItem>(){
        override fun areItemsTheSame(oldItem: VenueDetailsItem, newItem: VenueDetailsItem): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: VenueDetailsItem, newItem: VenueDetailsItem): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of venueDetailsItemList
    var venueDetailsItemList:List<VenueDetailsItem>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding=VenueDetailsCategoryRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VenueDetailsCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return venueDetailsItemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val venueDetailsItem=venueDetailsItemList[position]
        when(viewType){
            VenueMenuConstants.RestaurantMenu->{
                (holder as VenueDetailsCategoryViewHolder).bindRestaurantMenu(venueDetailsItem)
            }
            VenueMenuConstants.StoreMenuCategory->{
                (holder as VenueDetailsCategoryViewHolder).bindStoreMenuCategory(venueDetailsItem)
            }
            ItemSearchConstants.ItemSearchStoreCategories->{
                (holder as VenueDetailsCategoryViewHolder).bindStoreMenuCategory(venueDetailsItem)
            }
        }
    }
}