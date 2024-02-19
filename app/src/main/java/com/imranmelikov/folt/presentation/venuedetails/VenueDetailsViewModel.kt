package com.imranmelikov.folt.presentation.venuedetails

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
class VenueDetailsViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {

    private val storeMenuCategoryMutableLiveData=MutableLiveData<Resource<List<VenueDetailsItem>>>()
    val storeMenuCategoryLiveData:LiveData<Resource<List<VenueDetailsItem>>>
        get() = storeMenuCategoryMutableLiveData

    private val restaurantMenuMutableLiveData=MutableLiveData<Resource<List<VenueDetailsItem>>>()
    val restaurantMenuLiveData:LiveData<Resource<List<VenueDetailsItem>>>
        get() = restaurantMenuMutableLiveData

    private val exceptionHandlerRestaurant = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        restaurantMenuMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    private val exceptionHandlerStore = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        storeMenuCategoryMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    fun getRestaurantMenuList(){
        restaurantMenuMutableLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerRestaurant){
            val response=repository.getVenueDetailsItemRestaurant()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerRestaurant){
                response.data?.let {
                    restaurantMenuMutableLiveData.value=Resource.success(it)
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
}