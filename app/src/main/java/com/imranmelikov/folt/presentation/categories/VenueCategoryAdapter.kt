package com.imranmelikov.folt.presentation.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.VenueCategoryRvBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.VenueCategoryConstants

class VenueCategoryAdapter:RecyclerView.Adapter<VenueCategoryAdapter.VenueCategoryViewHolder>() {
    class VenueCategoryViewHolder(val binding:VenueCategoryRvBinding):RecyclerView.ViewHolder(binding.root)

     var venueList= listOf<Venue>()
    // viewType for Fragment
    var viewType:String=""
    // viewType for Venue
    var viewTypeVenue:String=""

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

    // Getter and setter for the list of VenueList
    var venueCategoryList:List<VenueCategory>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueCategoryViewHolder {
        val binding=VenueCategoryRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VenueCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return venueCategoryList.size
    }

    override fun onBindViewHolder(holder: VenueCategoryViewHolder, position: Int) {
         val venueCategoryArraylist=venueCategoryList[position]
         holder.binding.categoryName.text=venueCategoryArraylist.title
//         Glide.with(holder.itemView.context).load(venueCategoryArraylist.image)
//             .into(holder.binding.categoryImage)
            val filteredVenueList=venueList.filter {it.type==venueCategoryArraylist.title}
        holder.binding.categoryCount.text=filteredVenueList.size.toString()

        val bundleRestaurant = Bundle().apply {
            putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
            putSerializable(VenueCategoryConstants.venueCategories,venueCategoryArraylist)
            putString(VenueCategoryConstants.Venue,VenueCategoryConstants.Restaurant)
        }
        val bundleStore = Bundle().apply {
            putSerializable(VenueConstants.venues, ArrayList(filteredVenueList))
            putSerializable(VenueCategoryConstants.venueCategories,venueCategoryArraylist)
            putString(VenueCategoryConstants.Venue,VenueCategoryConstants.Store)
        }
        holder.itemView.setOnClickListener {
            when (viewType) {
                VenueCategoryConstants.Restaurant -> {
                    Navigation.findNavController(it).navigate(R.id.action_restaurantFragment_to_venueFragment,bundleRestaurant)
                }
                VenueCategoryConstants.Category -> {
                    when(viewTypeVenue){
                        VenueCategoryConstants.Restaurant->{
                            Navigation.findNavController(it).navigate(R.id.action_categoriesFragment_to_venueFragment,bundleRestaurant)
                        }
                        VenueCategoryConstants.Store->{
                            Navigation.findNavController(it).navigate(R.id.action_categoriesFragment_to_venueFragment,bundleStore)
                        }
                    }
                }
                VenueCategoryConstants.Store -> {
                    Navigation.findNavController(it).navigate(R.id.action_storeFragment_to_venueFragment,bundleStore)
                }
                VenueCategoryConstants.Discovery->{
                    Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_venueFragment,bundleRestaurant)
                }
            }
        }
    }
}