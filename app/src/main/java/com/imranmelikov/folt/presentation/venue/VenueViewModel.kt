package com.imranmelikov.folt.presentation.venue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.domain.model.CRUD
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {
    private val mutableVenueLiveData= MutableLiveData<Resource<List<Venue>>>()
    val venueLiveData: LiveData<Resource<List<Venue>>>
        get() = mutableVenueLiveData

    private val exceptionHandlerVenue = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableVenueLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    private val mutableFavoriteVenueLiveData= MutableLiveData<Resource<List<Venue>>>()
    val favoriteVenueLiveData: LiveData<Resource<List<Venue>>>
        get() = mutableFavoriteVenueLiveData

    private val exceptionHandlerFavoriteVenue = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableFavoriteVenueLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    private val mutableMessageLiveData= MutableLiveData<Resource<CRUD>>()
    val msgLiveData: LiveData<Resource<CRUD>>
        get() = mutableMessageLiveData

    private val exceptionHandlerMessage = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableMessageLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    fun getVenues(){
        mutableVenueLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerVenue) {
            val response=repository.getVenue()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerVenue) {
                response.data?.let {
                    mutableVenueLiveData.value=Resource.success(it)
                }
            }
        }
    }
    fun getFavoriteVenues(userId:String) {
        mutableFavoriteVenueLiveData.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerFavoriteVenue) {
            val response = repository.getFavoriteVenue(userId)
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerFavoriteVenue) {
                response.data?.let {
                    mutableFavoriteVenueLiveData.value = Resource.success(it)
                }
            }
        }
    }
    fun insertFavoriteVenue(venue: Venue,userId: String){
        mutableMessageLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerMessage){
            val response=repository.insertFavoriteVenue(venue,userId)
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerMessage){
                response.data?.let {
                    mutableMessageLiveData.value=Resource.success(it)
                }
            }
        }
        getFavoriteVenues(userId)
    }
    fun deleteFavoriteVenue(documentId:String,userId: String){
        mutableMessageLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerMessage){
            val response=repository.deleteFavoriteVenue(documentId,userId)
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerMessage){
                response.data?.let {
                    mutableMessageLiveData.value=Resource.success(it)
                }
            }
        }
        getFavoriteVenues(userId)
    }
}