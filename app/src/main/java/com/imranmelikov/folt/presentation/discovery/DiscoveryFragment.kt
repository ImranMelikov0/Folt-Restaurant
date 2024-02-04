package com.imranmelikov.folt.presentation.discovery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.databinding.FragmentDiscoveryBinding
import com.imranmelikov.folt.domain.model.Banner
import com.imranmelikov.folt.domain.model.DiscoveryItem
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.presentation.categories.VenueCategoryViewModel
import com.imranmelikov.folt.presentation.restaurants.RestaurantViewModel
import com.imranmelikov.folt.presentation.stores.StoreViewModel
import com.imranmelikov.folt.util.DiscoveryTitles
import com.imranmelikov.folt.util.ViewTypeDiscovery
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class DiscoveryFragment : Fragment() {
    private lateinit var binding:FragmentDiscoveryBinding
    private lateinit var discoveryViewModel:DiscoveryViewModel
    private lateinit var restaurantViewModel:RestaurantViewModel
    private lateinit var storeViewModel:StoreViewModel
    private lateinit var venueCategoryViewModel: VenueCategoryViewModel
    private lateinit var discoveryAdapter:DiscoveryAdapter
    private lateinit var discoveryItemParentStore:DiscoveryItem
    private lateinit var discoveryItemOfferStore:DiscoveryItem
    private lateinit var discoveryItemOfferRestaurant:DiscoveryItem
    private lateinit var discoveryItemCategory:DiscoveryItem
    private lateinit var discoveryItemDelivery:DiscoveryItem
    private lateinit var discoveryItemPopularity:DiscoveryItem
    private lateinit var discoveryItemFastest:DiscoveryItem
    private lateinit var discoveryItemYourFavRestaurants:DiscoveryItem
    private lateinit var discoveryItemYourFavStores:DiscoveryItem
    private lateinit var discoveryItemRating:DiscoveryItem
    private lateinit var discoveryItemParentRestaurant:DiscoveryItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDiscoveryBinding.inflate(inflater,container,false)
        discoveryViewModel=ViewModelProvider(requireActivity())[DiscoveryViewModel::class.java]
        restaurantViewModel=ViewModelProvider(requireActivity())[RestaurantViewModel::class.java]
        storeViewModel=ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        venueCategoryViewModel=ViewModelProvider(requireActivity())[VenueCategoryViewModel::class.java]

        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        discoveryViewModel.getParentRestaurant()
        discoveryViewModel.getParentStore()
        discoveryViewModel.getOfferStore()
        discoveryViewModel.getOfferRestaurant()
        discoveryViewModel.getSliderImageList()
        venueCategoryViewModel.getRestaurantCategories()
        restaurantViewModel.getVenues()
        storeViewModel.getVenues()
        observeRestaurant()
        observeSliderImageList()
    }
    private fun initialiseViewPager(bannerList:List<Banner>){
        for (banner in bannerList){
            binding.carousel.addData(CarouselItem(banner.image))
        }
    }
    private fun observeSliderImageList(){
        discoveryViewModel.sliderImageLiveData.observe(viewLifecycleOwner){
            initialiseViewPager(it)
        }
    }
    private fun observeParentRestaurant(venueList:List<Venue>){
        discoveryViewModel.parentRestaurantLiveData.observe(viewLifecycleOwner){parentVenues->
            val filteredPopular=parentVenues.filter { it.popularity }
             discoveryItemParentRestaurant=DiscoveryItem(DiscoveryTitles.popularRestaurants,ViewTypeDiscovery.ParentRestaurant,filteredPopular, venueList)
            observeOfferRestaurant(venueList)
        }
    }
    private fun observeParentStore(venueList: List<Venue>,restaurant:List<Venue>){
        discoveryViewModel.parentStoreLiveData.observe(viewLifecycleOwner){parentVenues->
            val filteredPopular=parentVenues.filter { it.popularity }
             discoveryItemParentStore=DiscoveryItem(DiscoveryTitles.popularStores,ViewTypeDiscovery.ParentStore,filteredPopular, venueList)
            val filteredStoreFavList=venueList.filter { it.venuePopularity.favorite }
            discoveryItemYourFavStores= DiscoveryItem(DiscoveryTitles.yourFavStores,ViewTypeDiscovery.VenueStore,filteredStoreFavList)
            observeParentRestaurant(restaurant)
        }
    }
    private fun observeOfferRestaurant(venueList: List<Venue>){
        discoveryViewModel.offerRestaurantLiveData.observe(viewLifecycleOwner){offerList->
            val filteredKfc=offerList.filter { it.parentVenue=="Restaurant"}
             discoveryItemOfferRestaurant=DiscoveryItem(DiscoveryTitles.restaurantOffers,ViewTypeDiscovery.OfferStore,filteredKfc,venueList,0)
            observeVenueCategory(venueList)
        }
    }
    private fun observeOfferStore(venueList: List<Venue>,restaurant: List<Venue>){
        discoveryViewModel.offerStoreLiveData.observe(viewLifecycleOwner){offerList->
            val filteredFoltMarket=offerList.filter { it.parentVenue=="Stores"}
            discoveryItemOfferStore=DiscoveryItem(DiscoveryTitles.foltMarketOffers,ViewTypeDiscovery.OfferStore,filteredFoltMarket,venueList,0)
            observeParentStore(venueList,restaurant)
        }
    }
    private fun observeVenueCategory(venueList: List<Venue>){
        venueCategoryViewModel.restaurantCategoryLiveData.observe(viewLifecycleOwner){categories->
            discoveryItemCategory=DiscoveryItem(DiscoveryTitles.categories,ViewTypeDiscovery.Category,categories, venueList,false)
            initialiseDiscoveryRv()
        }
    }
    private fun observeRestaurant(){
        restaurantViewModel.venueLiveData.observe(viewLifecycleOwner){restaurants->
            val filteredDelivery=restaurants.filter {  it.delivery.deliveryPrice.toDouble() == 0.00}
             discoveryItemDelivery=DiscoveryItem(DiscoveryTitles.deliveryFee,ViewTypeDiscovery.VenueRestaurant,filteredDelivery)
            val filteredPopularity=restaurants.filter { it.venuePopularity.popularity }
            discoveryItemPopularity=DiscoveryItem(DiscoveryTitles.popularRightNow,ViewTypeDiscovery.VenueRestaurant,filteredPopularity)
            val filteredFastestDelivery=restaurants.filter { it.delivery.deliveryTime.toDouble()<=20 }
             discoveryItemFastest=DiscoveryItem(DiscoveryTitles.fastestDelivery,ViewTypeDiscovery.VenueRestaurant,filteredFastestDelivery)
            val filteredYourFav=restaurants.filter { it.venuePopularity.favorite }
             discoveryItemYourFavRestaurants=DiscoveryItem(DiscoveryTitles.yourFavRestaurants,ViewTypeDiscovery.VenueRestaurant,filteredYourFav)
            val filteredTopRatedList=restaurants.filter { it.venuePopularity.rating>=9.0 }
             discoveryItemRating=DiscoveryItem(DiscoveryTitles.top_rated,ViewTypeDiscovery.VenueRestaurant,filteredTopRatedList)

            observeStore(restaurants)

        }
    }
    private fun observeStore(venueList: List<Venue>){
        storeViewModel.venueLiveData.observe(viewLifecycleOwner){stores->
            observeOfferStore(stores,venueList)
        }
    }
    private fun initialiseDiscoveryRv(){
        discoveryAdapter= DiscoveryAdapter()
        binding.eachRv.layoutManager=LinearLayoutManager(requireContext())
        val discoveryItemList= listOf(discoveryItemDelivery,discoveryItemPopularity,discoveryItemOfferStore,discoveryItemOfferRestaurant,
            discoveryItemFastest,discoveryItemParentRestaurant,discoveryItemParentStore,
            discoveryItemYourFavRestaurants,discoveryItemYourFavStores,discoveryItemCategory,discoveryItemRating)
        discoveryAdapter.discoveryItemList=discoveryItemList
        binding.eachRv.adapter=discoveryAdapter
    }
}