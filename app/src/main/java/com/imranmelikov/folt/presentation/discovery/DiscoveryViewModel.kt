package com.imranmelikov.folt.presentation.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.domain.model.Banner
import com.imranmelikov.folt.domain.model.Offer
import com.imranmelikov.folt.domain.model.ParentVenue

class DiscoveryViewModel:ViewModel() {
    private val sliderImageMutableLiveData=MutableLiveData<List<Banner>>()
    val sliderImageLiveData:LiveData<List<Banner>>
       get() = sliderImageMutableLiveData

    private val parentRestaurantMutableLiveData=MutableLiveData<List<ParentVenue>>()
    val parentRestaurantLiveData:LiveData<List<ParentVenue>>
        get() = parentRestaurantMutableLiveData

    private val parentStoreMutableLiveData=MutableLiveData<List<ParentVenue>>()
    val parentStoreLiveData:LiveData<List<ParentVenue>>
        get() = parentStoreMutableLiveData

    private val offerRestaurantMutableLiveData=MutableLiveData<List<Offer>>()
    val offerRestaurantLiveData:LiveData<List<Offer>>
        get() = offerRestaurantMutableLiveData

    private val offerStoreMutableLiveData=MutableLiveData<List<Offer>>()
    val offerStoreLiveData:LiveData<List<Offer>>
        get() = offerStoreMutableLiveData

    fun getSliderImageList(){
        val banner1=Banner(1,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","title","text",true)
        val banner2=Banner(2,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","title","text",true)
        val banner3=Banner(3,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","title","text",true)
        val bannerList= listOf(banner1,banner2,banner3)
        sliderImageMutableLiveData.value=bannerList
    }
    fun getParentRestaurant(){
        val parentVenue=ParentVenue(1,"restaurant","",true,true)
        val parentVenue2=ParentVenue(2,"restaurant2","",false,true)
        val parentVenue3=ParentVenue(3,"restaurant3","",true,true)
        val parentVenueList= listOf(parentVenue2,parentVenue,parentVenue3)
        val filteredParentVenueList=parentVenueList.filter { it.bribe }
        parentRestaurantMutableLiveData.value=filteredParentVenueList
    }
    fun getParentStore(){
        val parentVenue=ParentVenue(4,"store","",true,true)
        val parentVenue2=ParentVenue(5,"store2","",false,true)
        val parentVenue3=ParentVenue(6,"store3","",true, true)
        val parentVenueList= listOf(parentVenue2,parentVenue,parentVenue3)
        val filteredParentVenueList=parentVenueList.filter { it.bribe }
        parentStoreMutableLiveData.value=filteredParentVenueList
    }
    fun getOfferStore(){
        val banner=Banner(1,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","rest","offer",true)
        val banner2=Banner(2,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","rest","offer",true)
        val banner3=Banner(3,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","rest","offer",true)
        val offer=Offer(banner,"Stores")
        val offer2=Offer(banner2,"Stores")
        val offer3=Offer(banner3,"Stor")
        val offerList= listOf(offer2,offer3,offer)
        val filteredOfferList=offerList.filter { it.banner.bribe }
        offerStoreMutableLiveData.value=filteredOfferList
    }
    fun getOfferRestaurant(){
        val banner=Banner(4,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","banner","offer",true)
        val banner2=Banner(5,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","banner","offer",true)
        val banner3=Banner(6,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","banner","offer",true)
        val offer=Offer(banner,"Restaurant")
        val offer2=Offer(banner2,"Restaurant")
        val offer3=Offer(banner3,"Restaurant")
        val offerList= listOf(offer2,offer3,offer)
        val filteredOfferList=offerList.filter { it.banner.bribe }
        offerRestaurantMutableLiveData.value=filteredOfferList
    }
}