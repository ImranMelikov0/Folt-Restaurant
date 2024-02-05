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
import com.imranmelikov.folt.databinding.MainCategoryRvBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory

class MainCategoryAdapter:RecyclerView.Adapter<MainCategoryAdapter.MainCategoryViewHolder>() {
    class MainCategoryViewHolder(val binding:MainCategoryRvBinding):RecyclerView.ViewHolder(binding.root)

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryViewHolder {
        val binding=MainCategoryRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MainCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return venueCategoryList.size
    }

    override fun onBindViewHolder(holder: MainCategoryViewHolder, position: Int) {
        val venueCategory=venueCategoryList[position]
        holder.binding.categoryName.text=venueCategory.title

        val filteredVenueList=venueList.filter {it.type==venueCategory.title}

        val bundleStore = Bundle().apply {
            putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
            putSerializable(VenueCategoryConstants.venueCategories,venueCategory)
            putString(VenueCategoryConstants.Venue, VenueCategoryConstants.Store)
        }
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_venueFragment,bundleStore)
        }
    }
}