package com.imranmelikov.folt.presentation.venuedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.ItemSearchConstants
import com.imranmelikov.folt.databinding.StoreMenuCategoryRvBinding
import com.imranmelikov.folt.domain.model.StoreMenuCategory
import com.imranmelikov.folt.constants.StoreCategoryTitle
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.VenueMenuConstants

class StoreMenuCategoryAdp:RecyclerView.Adapter<StoreMenuCategoryAdp.StoreMenuCategoryViewHolder>() {
    class StoreMenuCategoryViewHolder(val binding:StoreMenuCategoryRvBinding):RecyclerView.ViewHolder(binding.root)

    var viewType=-45
    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<StoreMenuCategory>(){
        override fun areItemsTheSame(oldItem: StoreMenuCategory, newItem: StoreMenuCategory): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: StoreMenuCategory, newItem: StoreMenuCategory): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of CategoryList
    var storeMenuCategoryList:List<StoreMenuCategory>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreMenuCategoryViewHolder {
        val binding=StoreMenuCategoryRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoreMenuCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return storeMenuCategoryList.size
    }

    override fun onBindViewHolder(holder: StoreMenuCategoryViewHolder, position: Int) {
        val categoryList=storeMenuCategoryList[position]
        holder.binding.categoryName.text=categoryList.title
        Glide.with(holder.itemView.context)
            .load(categoryList.image)
            .into(holder.binding.categoryImage)
        val bundle=Bundle()
        bundle.apply {
            putInt(StoreCategoryTitle.storeCategoryTitle,categoryList.id)
        }

        holder.itemView.setOnClickListener {
            when(viewType){
                VenueMenuConstants.StoreMenuCategory->{
                    Navigation.findNavController(it).navigate(R.id.action_venueDetailsFragment_to_storeItemsFragment,bundle)
                }
                ItemSearchConstants.ItemSearchStoreCategories->{
                    Navigation.findNavController(it).navigate(R.id.action_itemSearchFragment_to_storeItemsFragment,bundle)
                }
            }

        }
    }
}