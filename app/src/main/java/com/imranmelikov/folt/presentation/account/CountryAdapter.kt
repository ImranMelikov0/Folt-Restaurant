package com.imranmelikov.folt.presentation.account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.databinding.CountryRvBinding
import com.imranmelikov.folt.domain.model.Country

class CountryAdapter:RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {
    class CountryViewHolder(val binding:CountryRvBinding):RecyclerView.ViewHolder(binding.root)

    var onItemClick:((Country)->Unit)?=null
    var countryName=""
    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<Country>(){
        override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter list country
    var countryList:List<Country>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding=CountryRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CountryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country=countryList[position]
        holder.binding.countryText.text=country.countryName

        if (country.countryName==countryName){
            holder.binding.selectedImg.visibility=View.VISIBLE
        }else{
            holder.binding.selectedImg.visibility=View.GONE
        }
        holder.itemView.setOnClickListener {
            onItemClick?.let {
                it(country)
            }
        }
    }
}