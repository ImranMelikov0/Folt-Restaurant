package com.imranmelikov.folt.presentation.storeitems

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.domain.model.VenueDetailsItem
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreItemsViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {
    private val storeMenuMutableLiveData=MutableLiveData<Resource<List<VenueDetailsItem>>>()
    val storeMenuLiveData:LiveData<Resource<List<VenueDetailsItem>>>
        get() = storeMenuMutableLiveData

    private val exceptionHandlerVenueDetails = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        storeMenuMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    fun getStoreMenuList() {
        storeMenuMutableLiveData.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerVenueDetails) {
            val response = repository.getVenueDetailsItemRestaurant()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerVenueDetails) {
                response.data?.let {
                    storeMenuMutableLiveData.value = Resource.success(it)
                }
            }
        }
    }
}