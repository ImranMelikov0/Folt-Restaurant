package com.imranmelikov.folt.presentation.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.DiscoveryCategoryMainRvBinding
import com.imranmelikov.folt.domain.model.DiscoveryItem
import com.imranmelikov.folt.presentation.categories.VenueCategoryAdapter
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.OfferConstants
import com.imranmelikov.folt.constants.ParentVenueConstants
import com.imranmelikov.folt.constants.VenueCategoryConstants
import com.imranmelikov.folt.constants.ViewTypeDiscovery

class DiscoveryAdapter:RecyclerView.Adapter<DiscoveryAdapter.DiscoveryViewHolder>() {
    class DiscoveryViewHolder(val binding:DiscoveryCategoryMainRvBinding):RecyclerView.ViewHolder(binding.root){
        fun bindOffer(discoveryItem: DiscoveryItem){
            discoveryItem.offerList?.let {offerList->
                if (offerList.isEmpty()){
                    binding.discoveryTitle.visibility=View.GONE
                    binding.seeAllBtn.visibility=View.GONE
                }
                discoveryItem.venueList?.let {venueList->
                    val adapter=OfferAdapter()
                    binding.categoryInlineRv.layoutManager=LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
                    adapter.viewTypeFragment=ParentVenueConstants.Discovery
                    adapter.offerList=offerList
                    adapter.venueList=venueList
                    binding.categoryInlineRv.adapter=adapter

                    val bundle=Bundle()
                    bundle.apply {
                        putString(VenueCategoryConstants.VenueCategoryTitle,OfferConstants.OfferTitle)
                        putSerializable(OfferConstants.offer, ArrayList(offerList))
                        putSerializable(VenueConstants.venues, ArrayList(venueList))
                        putString(ParentVenueConstants.titleString,discoveryItem.title)
                    }
                    binding.seeAllBtn.setOnClickListener {
                       Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_categoriesFragment,bundle)
                    }
                }
            }
        }
        fun bindParentVenue(discoveryItem: DiscoveryItem){
            discoveryItem.parentVenueList?.let {parentVenue->
                if (parentVenue.isEmpty()){
                    binding.discoveryTitle.visibility=View.GONE
                    binding.seeAllBtn.visibility=View.GONE
                }
                discoveryItem.venueList?.let {venueList->
                    val adapter= ParentVenueAdapter()
                    binding.categoryInlineRv.layoutManager=LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
                    adapter.viewTypeFragment=ParentVenueConstants.Discovery
                    adapter.parentVenueList=parentVenue
                    adapter.venueList=venueList
                    binding.categoryInlineRv.adapter=adapter

                    val bundle=Bundle()
                    bundle.apply {
                        putString(VenueCategoryConstants.VenueCategoryTitle,ParentVenueConstants.ParentVenueTitle)
                        putSerializable(ParentVenueConstants.parentVenues, ArrayList(parentVenue))
                        putSerializable(VenueConstants.venues, ArrayList(venueList))
                        putString(ParentVenueConstants.titleString,discoveryItem.title)
                    }
                    binding.seeAllBtn.setOnClickListener {
                         Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_categoriesFragment,bundle)
                    }
                }
            }
        }
        fun bindVenue(discoveryItem: DiscoveryItem){
            discoveryItem.venueList?.let {venue->
                if (venue.isEmpty()){
                    binding.discoveryTitle.visibility=View.GONE
                    binding.seeAllBtn.visibility=View.GONE
                }
                val bundle = Bundle().apply {
                    putSerializable(VenueConstants.venues, ArrayList(venue))
                    putString(VenueCategoryConstants.DiscoveryTitle,discoveryItem.title)
                }

                val adapter=VenueReviewAdapter()
                binding.categoryInlineRv.layoutManager=LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
                adapter.viewType=discoveryItem.viewType
                adapter.venueList=venue
                binding.categoryInlineRv.adapter=adapter

                binding.seeAllBtn.setOnClickListener {
                    when(discoveryItem.viewType){
                        ViewTypeDiscovery.Venue->{
                                Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_venueFragment,bundle)
                        }
                        ViewTypeDiscovery.Profile->{
                            Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_venueFragment,bundle)
                        }
                    }
                }
            }
        }
        fun bindCategory(discoveryItem: DiscoveryItem){
            val bundle=Bundle()
            discoveryItem.venueCategoryList?.let {venueCategories ->
                if (venueCategories.isEmpty()){
                    binding.discoveryTitle.visibility=View.GONE
                    binding.seeAllBtn.visibility=View.GONE
                }
                discoveryItem.venueList?.let {venues ->
                    bundle.apply {
                            putString(VenueCategoryConstants.VenueCategoryTitle,VenueCategoryConstants.VenueCategoryTitle)
                            putSerializable(VenueCategoryConstants.venueCategories, ArrayList(venueCategories))
                            putSerializable(VenueConstants.venues, ArrayList(venues))
                    }
                    binding.seeAllBtn.setOnClickListener {
                        Navigation.findNavController(it).navigate(R.id.action_discoveryFragment_to_categoriesFragment,bundle)
                    }
                    val adapter=VenueCategoryAdapter()
                    binding.categoryInlineRv.layoutManager=LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
                        adapter.venueCategoryList=venueCategories
                        adapter.viewType=VenueCategoryConstants.Discovery
                        adapter.venueList=venues
                        binding.categoryInlineRv.adapter=adapter
                }
            }
        }
    }

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<DiscoveryItem>(){
        override fun areItemsTheSame(oldItem: DiscoveryItem, newItem: DiscoveryItem): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: DiscoveryItem, newItem: DiscoveryItem): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of discoveryItem
    var discoveryItemList:List<DiscoveryItem>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoveryViewHolder {
        val binding=DiscoveryCategoryMainRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DiscoveryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return discoveryItemList.size
    }

    override fun onBindViewHolder(holder: DiscoveryViewHolder, position: Int) {
        val discoveryItem=discoveryItemList[position]
        holder.binding.discoveryTitle.text=discoveryItem.title
        when(discoveryItem.viewType){
            ViewTypeDiscovery.Offer->{
                holder.bindOffer(discoveryItem)
            }
            ViewTypeDiscovery.ParentVenue->{
                holder.bindParentVenue(discoveryItem)
            }
            ViewTypeDiscovery.Venue->{
                holder.bindVenue(discoveryItem)
            }
            ViewTypeDiscovery.Category->{
                holder.bindCategory(discoveryItem)
            }
            ViewTypeDiscovery.Profile->{
                holder.bindVenue(discoveryItem)
            }
        }
    }
}