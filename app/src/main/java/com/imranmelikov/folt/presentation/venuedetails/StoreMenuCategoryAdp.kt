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
import com.imranmelikov.folt.databinding.StoreMenuCategoryRvBinding
import com.imranmelikov.folt.domain.model.StoreMenuCategory
import com.imranmelikov.folt.util.StoreCategoryName
import com.imranmelikov.folt.util.StoreCategoryTitle

class StoreMenuCategoryAdp:RecyclerView.Adapter<StoreMenuCategoryAdp.StoreMenuCategoryViewHolder>() {
    class StoreMenuCategoryViewHolder(val binding:StoreMenuCategoryRvBinding):RecyclerView.ViewHolder(binding.root)

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
        holder.binding.categoryName.text=categoryList.restaurantMenuCategory.title
        Glide.with(holder.itemView.context)
            .load(categoryList.image)
            .into(holder.binding.categoryImage)
        val bundle=Bundle()
        bundle.apply {
            putInt(StoreCategoryTitle.storeCategoryTitle,categoryList.restaurantMenuCategory.id)
            putString(StoreCategoryName.categoryName,categoryList.restaurantMenuCategory.title)
        }

        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_venueDetailsFragment_to_storeItemsFragment,bundle)
        }
    }
}