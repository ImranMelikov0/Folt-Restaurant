package com.imranmelikov.folt.presentation.venuedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.databinding.CategoriesHorizontalTopRvBinding

class RestaurantMenuCategoryAdp:RecyclerView.Adapter<RestaurantMenuCategoryAdp.RestaurantMenuCategoryViewHolder>() {
    class RestaurantMenuCategoryViewHolder(val binding:CategoriesHorizontalTopRvBinding):RecyclerView.ViewHolder(binding.root)

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of CategoryTitleList
    var categoryNameList:List<String>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuCategoryViewHolder {
        val binding=CategoriesHorizontalTopRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RestaurantMenuCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryNameList.size
    }

    override fun onBindViewHolder(holder: RestaurantMenuCategoryViewHolder, position: Int) {
        holder.binding.categoryName.text=categoryNameList[position]
    }
}