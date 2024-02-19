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
    private val sliderImageMutableLiveData=MutableLiveData<Resource<List<Banner>>>()
    val sliderImageLiveData:LiveData<Resource<List<Banner>>>
       get() = sliderImageMutableLiveData

    private val parentVenueMutableLiveData=MutableLiveData<Resource<List<ParentVenue>>>()
    val parentVenueLiveData:LiveData<Resource<List<ParentVenue>>>
        get() = parentVenueMutableLiveData

    private val offersMutableLiveData=MutableLiveData<Resource<List<Offer>>>()
    val offersLiveData:LiveData<Resource<List<Offer>>>
        get() = offersMutableLiveData

    private val exceptionHandlerOffer = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        offersMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    private val exceptionHandlerBanner = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        sliderImageMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    private val exceptionHandlerParentVenue = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        parentVenueMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    fun getSliderImageList(){
        sliderImageMutableLiveData.value=Resource.loading(null)
        viewModelScope.launch (Dispatchers.IO + exceptionHandlerBanner ){
            val response=foltRepo.getBanner()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerBanner){
                response.data?.let {
                    sliderImageMutableLiveData.value=Resource.success(it)
                }
            }
        }
    }
    fun getParentVenue(){
        parentVenueMutableLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerParentVenue) {
            val response= foltRepo.getParentVenue()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerParentVenue){
                response.data?.let {parentVenueList->
                    val filteredParentVenueList=parentVenueList.filter { it.bribe }
                    parentVenueMutableLiveData.value=Resource.success(filteredParentVenueList)
                }
            }
        }
    }
    fun getOffers(){
        offersMutableLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerOffer) {
            val response=foltRepo.getOffer()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerOffer){
                response.data?.let {
                    offersMutableLiveData.value=Resource.success(it)
                }
            }
        }
    }
}