package com.imranmelikov.folt.presentation.venuedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.data.local.entity.VenueDetailsRoom
import com.imranmelikov.folt.domain.model.CRUD
import com.imranmelikov.folt.domain.model.VenueDetails
import com.imranmelikov.folt.domain.model.VenueDetailsItem
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueDetailsViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {

    private val storeMenuCategoryMutableLiveData=MutableLiveData<Resource<List<VenueDetailsItem>>>()
    val storeMenuCategoryLiveData:LiveData<Resource<List<VenueDetailsItem>>>
        get() = storeMenuCategoryMutableLiveData

    private val venueMenuMutableLiveData=MutableLiveData<Resource<List<VenueDetailsItem>>>()
    val venueMenuLiveData:LiveData<Resource<List<VenueDetailsItem>>>
        get() = venueMenuMutableLiveData

    private val mutableVenueDetailsLiveData= MutableLiveData<List<VenueDetailsRoom>>()
    val venueDetailsLiveData: LiveData<List<VenueDetailsRoom>>
        get() = mutableVenueDetailsLiveData

    private val mutableStockMessageLiveData= MutableLiveData<Resource<CRUD>>()
    val msgStockLiveData: LiveData<Resource<CRUD>>
        get() = mutableStockMessageLiveData

    private val exceptionHandlerMessage = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableStockMessageLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    private val exceptionHandlerVenue = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        venueMenuMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    private val exceptionHandlerStore = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        storeMenuCategoryMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    fun getVenueMenuList(){
        venueMenuMutableLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerVenue){
            val response=repository.getVenueDetailsItemVenue()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerVenue){
                response.data?.let {
                    venueMenuMutableLiveData.value=Resource.success(it)
                }
            }
        }
    }
    fun getStoreMenuCategoryList(){
        storeMenuCategoryMutableLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerStore){
           val response= repository.getVenueDetailsItemStore()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerStore){
                response.data?.let {
                    storeMenuCategoryMutableLiveData.value=Resource.success(it)
                }
            }
        }
    }
    fun updateVenueDetailStock(documentId:String,venueDetailsList: List<VenueDetails>){
        mutableStockMessageLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerMessage){
            val response=repository.updateVenueDetailsStock(documentId,venueDetailsList)
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerMessage){
                response.data?.let {
                    mutableStockMessageLiveData.value=Resource.success(it)
                }
            }
        }
        getVenueMenuList()
    }

    fun insertVenueDetailsToRoom(venueDetailsRoom: VenueDetailsRoom){
        viewModelScope.launch {
            repository.insertVenueDetailsToRoom(venueDetailsRoom)
        }
        getVenueDetailsFromRoom()
    }
    fun deleteVenueDetailsFromRoom(venueDetailsRoom: VenueDetailsRoom){
        viewModelScope.launch{
            repository.deleteVenueDetailsFromRoom(venueDetailsRoom)
        }
        getVenueDetailsFromRoom()
    }
    fun updateVenueDetailsFromRoom(venueDetailsRoom: VenueDetailsRoom){
        viewModelScope.launch {
            repository.updateVenueDetailsFromRoom(venueDetailsRoom)
        }
        getVenueDetailsFromRoom()
    }
    fun deleteAllVenueDetailsFromRoom(){
        viewModelScope.launch {
            repository.deleteAllVenueDetailsFromRoom()
        }
        getVenueDetailsFromRoom()
    }
    fun getVenueDetailsFromRoom(){
        viewModelScope.launch {
            mutableVenueDetailsLiveData.value=repository.getVenueDetailsFromRoom()
        }
    }
}