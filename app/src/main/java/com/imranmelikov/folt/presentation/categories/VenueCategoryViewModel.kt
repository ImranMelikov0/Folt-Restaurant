package com.imranmelikov.folt.presentation.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.domain.model.VenueCategory
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueCategoryViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {
    private val mutableVenueCategoryLiveData= MutableLiveData<Resource<List<VenueCategory>>>()
    val venueCategoryLiveData: LiveData<Resource<List<VenueCategory>>>
        get() = mutableVenueCategoryLiveData

    private val exceptionHandlerVenueCategory = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableVenueCategoryLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    fun getVenueCategories(){
        mutableVenueCategoryLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerVenueCategory){
            val response=repository.getVenueCategory()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerVenueCategory){
                response.data?.let {
                    mutableVenueCategoryLiveData.value=Resource.success(it)
                }
            }
        }
    }
}