package com.imranmelikov.folt.presentation.itemsearch

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
class ItemSearchViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {

    private var storeMenuCategoryMutableLiveData=MutableLiveData<Resource<List<VenueDetailsItem>>>()
    val storeMenuCategoryLiveData:LiveData<Resource<List<VenueDetailsItem>>>
        get() = storeMenuCategoryMutableLiveData

    private var venueMenuMutableLiveData=MutableLiveData<Resource<List<VenueDetailsItem>>>()
    val venueMenuLiveData:LiveData<Resource<List<VenueDetailsItem>>>
        get() = venueMenuMutableLiveData

    private val exceptionHandlerVenue = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        venueMenuMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    private val exceptionHandlerStore = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        storeMenuCategoryMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    fun searchVenueMenuList(searchString: String){
        venueMenuMutableLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerVenue){
            val response=repository.searchVenueDetailsItemVenue(searchString)
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerVenue){
                response.data?.let {
                    venueMenuMutableLiveData.value=Resource.success(it)
                }
            }
        }
    }
    fun searchStoreMenuCategoryList(searchString: String){
        storeMenuCategoryMutableLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerStore){
            val response= repository.searchVenueDetailsItemStore(searchString)
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerStore){
                response.data?.let {
                    storeMenuCategoryMutableLiveData.value=Resource.success(it)
                }
            }
        }
    }
    fun clearVenueMenuMutableList(){
        venueMenuMutableLiveData.value=Resource.success(null)
        venueMenuMutableLiveData=MutableLiveData<Resource<List<VenueDetailsItem>>>()
    }
    fun clearStoreMenuMutableList(){
        storeMenuCategoryMutableLiveData.value=Resource.success(null)
        storeMenuCategoryMutableLiveData=MutableLiveData<Resource<List<VenueDetailsItem>>>()
    }
}