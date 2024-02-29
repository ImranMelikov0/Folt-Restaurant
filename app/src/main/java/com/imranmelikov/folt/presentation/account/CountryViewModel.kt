package com.imranmelikov.folt.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.data.local.entity.CountryRoom
import com.imranmelikov.folt.domain.model.Country
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.domain.use_case.GetCountries
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(private val countryUseCase:GetCountries,private val repository: FoltRepository):ViewModel() {
    private val mutableCountriesLiveData=MutableLiveData<Resource<List<Country>>>()
    val countriesLiveData:LiveData<Resource<List<Country>>>
        get() = mutableCountriesLiveData

    private val exceptionHandlerCountries= CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableCountriesLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    private val mutableCountryLiveData=MutableLiveData<List<CountryRoom>>()
    val countryLiveData:LiveData<List<CountryRoom>>
        get() = mutableCountryLiveData

    fun getCountries(){
        mutableCountriesLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerCountries){
            val response=countryUseCase.getCountries()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerCountries){
                response.data?.let {
                    val filteredList=it.filterIndexed{index,_->
                        index!=11
                    }
                    mutableCountriesLiveData.value=Resource.success(filteredList)
                }
            }
        }
    }
    fun getCountry(){
        viewModelScope.launch {
            val response=repository.getCountry()
           mutableCountryLiveData.value=response
        }
    }
    fun insertCountry(countryRoom: CountryRoom){
        viewModelScope.launch {
            repository.insertCountry(countryRoom)
        }
        getCountry()
    }
    fun deleteCountries(){
        viewModelScope.launch {
            repository.deleteAllCountry()
        }
        getCountry()
    }
}