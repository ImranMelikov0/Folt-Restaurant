package com.imranmelikov.folt.presentation.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.VenueCategoryConstants
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.databinding.CategoriesHorizontalTopRvBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory

class MainCategoryHorizontalAdapter:RecyclerView.Adapter<MainCategoryHorizontalAdapter.RestaurantMenuCategoryViewHolder>() {
    class RestaurantMenuCategoryViewHolder(val binding:CategoriesHorizontalTopRvBinding):RecyclerView.ViewHolder(binding.root)

    var venueList= listOf<Venue>()

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<VenueCategory>(){
        override fun areItemsTheSame(oldItem: VenueCategory, newItem: VenueCategory): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: VenueCategory, newItem: VenueCategory): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of VenueCategoryList
    var venueCategoryList:List<VenueCategory>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuCategoryViewHolder {
        val binding=CategoriesHorizontalTopRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RestaurantMenuCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return venueCategoryList.size
    }

    override fun onBindViewHolder(holder: RestaurantMenuCategoryViewHolder, position: Int) {
        val venueCategory=venueCategoryList[position]
        holder.binding.categoryName.text=venueCategory.title

        val filteredVenueList=venueList.filter {it.type==venueCategory.title}

        val bundle = Bundle().apply {
            putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
            putSerializable(VenueCategoryConstants.venueCategories,venueCategory)
        }
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_venueFragment,bundle)
        }
    }
}