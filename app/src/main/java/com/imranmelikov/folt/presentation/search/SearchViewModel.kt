package com.imranmelikov.folt.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {
    private var mutableVenueLiveData= MutableLiveData<Resource<List<Venue>>>()
    val venueLiveData: LiveData<Resource<List<Venue>>>
        get() = mutableVenueLiveData

    private val exceptionHandlerVenue = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableVenueLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    fun searchVenues(searchText:String){
        mutableVenueLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerVenue) {
            val response=repository.searchVenue(searchText)
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerVenue) {
                response.data?.let {
                    mutableVenueLiveData.value=Resource.success(it)
                }
            }
        }
    }
}