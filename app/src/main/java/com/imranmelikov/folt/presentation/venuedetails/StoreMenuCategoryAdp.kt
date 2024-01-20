package com.imranmelikov.folt.presentation.venuedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imranmelikov.folt.databinding.StoreMenuCategoryRvBinding
import com.imranmelikov.folt.domain.model.StoreMenuCategory

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
        holder.binding.itemPrice.visibility=View.GONE
        Glide.with(holder.itemView.context)
            .load(categoryList.image)
            .into(holder.binding.categoryImage)
    }
}