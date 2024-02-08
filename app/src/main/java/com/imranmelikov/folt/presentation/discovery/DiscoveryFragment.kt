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
import com.imranmelikov.folt.constants.DiscoveryTitles
import com.imranmelikov.folt.constants.ViewTypeDiscovery
import com.imranmelikov.folt.domain.model.VenueCategory
import com.imranmelikov.folt.presentation.venue.VenueViewModel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class DiscoveryFragment : Fragment() {
    private lateinit var binding:FragmentDiscoveryBinding
    private lateinit var discoveryViewModel:DiscoveryViewModel
    private lateinit var venueViewModel:VenueViewModel
    private lateinit var venueCategoryViewModel: VenueCategoryViewModel
    private lateinit var discoveryAdapter:DiscoveryAdapter
    private lateinit var discoveryItemParentStore:DiscoveryItem
    private lateinit var discoveryItemOfferStore:DiscoveryItem
    private lateinit var discoveryItemOfferRestaurant:DiscoveryItem
    private lateinit var discoveryItemCategory:DiscoveryItem
    private lateinit var discoveryItemDelivery:DiscoveryItem
    private lateinit var discoveryItemPopularity:DiscoveryItem
    private lateinit var discoveryItemFastest:DiscoveryItem
    private lateinit var discoveryItemYourFav:DiscoveryItem
    private lateinit var discoveryItemRating:DiscoveryItem
    private lateinit var discoveryItemParentRestaurant:DiscoveryItem
    private lateinit var mainCategoryAdapter: MainCategoryAdapter
    private lateinit var mainCategoryHorizontalAdapter: MainCategoryHorizontalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDiscoveryBinding.inflate(inflater,container,false)
        discoveryViewModel=ViewModelProvider(requireActivity())[DiscoveryViewModel::class.java]
        venueViewModel=ViewModelProvider(requireActivity())[VenueViewModel::class.java]
        venueCategoryViewModel=ViewModelProvider(requireActivity())[VenueCategoryViewModel::class.java]

        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        discoveryViewModel.getParentVenue()
        discoveryViewModel.getOffers()
        discoveryViewModel.getSliderImageList()
        venueCategoryViewModel.getVenueCategories()
        venueViewModel.getVenues()
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
    private fun observeParentVenue(stores:List<Venue>,restaurants: List<Venue>){
        discoveryViewModel.parentVenueLiveData.observe(viewLifecycleOwner){parentVenues->
            val filteredPopularRestaurants=parentVenues.filter { it.popularity && it.restaurant }
            val filteredPopularStores=parentVenues.filter { it.popularity && !it.restaurant }
             discoveryItemParentRestaurant=DiscoveryItem(DiscoveryTitles.popularRestaurants,ViewTypeDiscovery.ParentVenue,filteredPopularRestaurants, restaurants)
             discoveryItemParentStore=DiscoveryItem(DiscoveryTitles.popularRestaurants,ViewTypeDiscovery.ParentVenue,filteredPopularStores, stores)
        }
    }
    private fun observeOffers(venues: List<Venue>){
        discoveryViewModel.offersLiveData.observe(viewLifecycleOwner){offerList->
            val filteredFoltMarket=offerList.filter { it.parentVenue=="Stores"}
            val filteredKfc=offerList.filter { it.parentVenue=="Restaurant"}
            discoveryItemOfferRestaurant=DiscoveryItem(DiscoveryTitles.restaurantOffers,ViewTypeDiscovery.Offer,filteredKfc,venues,0)
            discoveryItemOfferStore=DiscoveryItem(DiscoveryTitles.foltMarketOffers,ViewTypeDiscovery.Offer,filteredFoltMarket,venues,0)
        }
    }
    private fun observeVenueCategory(restaurants: List<Venue>,categories:List<VenueCategory>){
            val filteredCategoryList=categories.filter { it.restaurant }
            discoveryItemCategory=DiscoveryItem(DiscoveryTitles.categories,ViewTypeDiscovery.Category,filteredCategoryList, restaurants,false)
            initialiseDiscoveryRv()
    }
    private fun observeRestaurant(){
        venueViewModel.venueLiveData.observe(viewLifecycleOwner){venues->
            val restaurants=venues.filter { it.restaurant }
            val filteredDelivery=restaurants.filter {  it.delivery.deliveryPrice.toDouble() == 0.00 }
             discoveryItemDelivery=DiscoveryItem(DiscoveryTitles.deliveryFee,ViewTypeDiscovery.Venue,filteredDelivery)
            val filteredPopularity=restaurants.filter { it.venuePopularity.popularity  }
            discoveryItemPopularity=DiscoveryItem(DiscoveryTitles.popularRightNow,ViewTypeDiscovery.Venue,filteredPopularity)
            val filteredFastestDelivery=restaurants.filter { it.delivery.deliveryTime.toDouble()<=20  }
             discoveryItemFastest=DiscoveryItem(DiscoveryTitles.fastestDelivery,ViewTypeDiscovery.Venue,filteredFastestDelivery)
            val filteredYourFav=venues.filter { it.venuePopularity.favorite}
             discoveryItemYourFav=DiscoveryItem(DiscoveryTitles.yourFav,ViewTypeDiscovery.Venue,filteredYourFav)
            val filteredTopRatedList=restaurants.filter { it.venuePopularity.rating>=9.0 }
             discoveryItemRating=DiscoveryItem(DiscoveryTitles.top_rated,ViewTypeDiscovery.Venue,filteredTopRatedList)

            val stores=venues.filter { !it.restaurant }
            observeOffers(venues)
            observeParentVenue(stores,restaurants)
            initialiseMainCategoryRv(stores,restaurants)
        }
    }
    private fun initialiseDiscoveryRv(){
        discoveryAdapter= DiscoveryAdapter()
        binding.eachRv.layoutManager=LinearLayoutManager(requireContext())
        val discoveryItemList= listOf(discoveryItemDelivery,discoveryItemPopularity,discoveryItemOfferStore,discoveryItemOfferRestaurant,
            discoveryItemFastest,discoveryItemParentRestaurant,discoveryItemParentStore,
            discoveryItemYourFav,discoveryItemCategory,discoveryItemRating)
        discoveryAdapter.discoveryItemList=discoveryItemList
        binding.eachRv.adapter=discoveryAdapter
    }
    private fun initialiseMainCategoryRv(stores: List<Venue>,restaurants: List<Venue>){
        mainCategoryAdapter= MainCategoryAdapter()
        binding.mainCategoryRv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        venueCategoryViewModel.venueCategoryLiveData.observe(viewLifecycleOwner){categories->
            val filteredCategoryList=categories.filter { !it.restaurant }
            mainCategoryAdapter.venueCategoryList=filteredCategoryList
            mainCategoryAdapter.venueList=stores
            binding.mainCategoryRv.adapter=mainCategoryAdapter

            initialiseToolbarRv(stores,filteredCategoryList)
            observeVenueCategory(restaurants,categories)
        }
    }
    private fun initialiseToolbarRv(stores: List<Venue>, categoryList: List<VenueCategory>){
        mainCategoryHorizontalAdapter= MainCategoryHorizontalAdapter()
        binding.toolbarStoreCategoriesRv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        mainCategoryHorizontalAdapter.venueCategoryList=categoryList
        mainCategoryHorizontalAdapter.venueList=stores
        binding.toolbarStoreCategoriesRv.adapter=mainCategoryHorizontalAdapter
    }
}