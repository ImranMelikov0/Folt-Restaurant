package com.imranmelikov.folt.presentation.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.domain.model.Banner
import com.imranmelikov.folt.domain.model.Offer
import com.imranmelikov.folt.domain.model.ParentVenue
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val foltRepo:FoltRepository
):ViewModel() {
    private val sliderImageMutableLiveData=MutableLiveData<List<Banner>>()
    val sliderImageLiveData:LiveData<List<Banner>>
       get() = sliderImageMutableLiveData

    private val parentVenueMutableLiveData=MutableLiveData<List<ParentVenue>>()
    val parentVenueLiveData:LiveData<List<ParentVenue>>
        get() = parentVenueMutableLiveData

    private val offersMutableLiveData=MutableLiveData<Resource<List<Offer>>>()
    val offersLiveData:LiveData<Resource<List<Offer>>>
        get() = offersMutableLiveData

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        offersMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    fun getSliderImageList(){
        val banner1=Banner("1","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","title","text",true)
        val banner2=Banner("2","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","title","text",true)
        val banner3=Banner("3","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODugclY3VcoiUgyAa6xsmXGQkwQJzBOZMKw&usqp=CAU","title","text",true)
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
        offersMutableLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            val result=foltRepo.getOffer()
            result.data?.let {
                offersMutableLiveData.value=Resource.success(it)
            }
        }
    }
}