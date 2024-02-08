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

    private val parentVenueMutableLiveData=MutableLiveData<List<ParentVenue>>()
    val parentVenueLiveData:LiveData<List<ParentVenue>>
        get() = parentVenueMutableLiveData

    private val offersMutableLiveData=MutableLiveData<List<Offer>>()
    val offersLiveData:LiveData<List<Offer>>
        get() = offersMutableLiveData


    fun getSliderImageList(){
        val banner1=Banner(1,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","title","text",true)
        val banner2=Banner(2,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","title","text",true)
        val banner3=Banner(3,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","title","text",true)
        val bannerList= listOf(banner1,banner2,banner3)
        sliderImageMutableLiveData.value=bannerList
    }
    fun getParentVenue(){
        val parentVenue=ParentVenue(1,"restaurant","",true,true,true)
        val parentVenue2=ParentVenue(2,"restaurant2","",false,true,true)
        val parentVenue3=ParentVenue(3,"restaurant3","",true,true,true)
        val parentVenue4=ParentVenue(4,"store","",true,true,false)
        val parentVenue5=ParentVenue(5,"store2","",false,true,false)
        val parentVenue6=ParentVenue(6,"store3","",true, true,false)
        val parentVenueList= listOf(parentVenue2,parentVenue,parentVenue3,parentVenue4,parentVenue5,parentVenue6)
        val filteredParentVenueList=parentVenueList.filter { it.bribe }
        parentVenueMutableLiveData.value=filteredParentVenueList
    }
    fun getOffers(){
        val banner=Banner(1,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","rest","offer",true)
        val banner2=Banner(2,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","rest","offer",true)
        val banner3=Banner(3,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","rest","offer",true)
        val offer=Offer(banner,"Stores")
        val offer2=Offer(banner2,"Stores")
        val offer3=Offer(banner3,"Stor")
        val banner4=Banner(4,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","banner","offer",true)
        val banner5=Banner(5,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","banner","offer",true)
        val banner6=Banner(6,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","banner","offer",true)
        val offer4=Offer(banner4,"Restaurant")
        val offer5=Offer(banner5,"Restaurant")
        val offer6=Offer(banner6,"Restaurant")
        val offerList= listOf(offer2,offer3,offer,offer4,offer5,offer6)
        val filteredOfferList=offerList.filter { it.banner.bribe }
        offersMutableLiveData.value=filteredOfferList
    }
}