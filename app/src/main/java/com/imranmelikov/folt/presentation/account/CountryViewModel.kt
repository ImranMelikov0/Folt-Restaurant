package com.imranmelikov.folt.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.domain.model.Country
import com.imranmelikov.folt.domain.use_case.GetCountries
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(private val countryUseCase:GetCountries):ViewModel() {
    private val mutableCountryLiveData=MutableLiveData<Resource<List<Country>>>()
    val countryLiveData:LiveData<Resource<List<Country>>>
        get() = mutableCountryLiveData

    private val exceptionHandlerCountry = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableCountryLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    fun getCountries(){
        mutableCountryLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerCountry){
            val response=countryUseCase.getCountries()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerCountry){
                response.data?.let {
                    val filteredList=it.filterIndexed{index,_->
                        index!=11
                    }
                    mutableCountryLiveData.value=Resource.success(filteredList)
                }
            }
        }
    }
}