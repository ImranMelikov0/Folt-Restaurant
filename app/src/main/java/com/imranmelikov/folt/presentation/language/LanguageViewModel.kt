package com.imranmelikov.folt.presentation.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.data.local.entity.LanguageRoom
import com.imranmelikov.folt.domain.model.Language
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {

    private val mutableLanguagesLiveData= MutableLiveData<Resource<List<Language>>>()
    val languagesLiveData: LiveData<Resource<List<Language>>>
        get() = mutableLanguagesLiveData

    private val exceptionHandlerLanguages= CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableLanguagesLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    private val mutableLanguageLiveData= MutableLiveData<List<LanguageRoom>>()
    val languageLiveData: LiveData<List<LanguageRoom>>
        get() = mutableLanguageLiveData

    fun getLanguages(){
        mutableLanguagesLiveData.value= Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerLanguages){
            val response=repository.getLanguages()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerLanguages){
                response.data?.let {
                    mutableLanguagesLiveData.value= Resource.success(it)
                }
            }
        }
    }

    fun getLanguage(){
        viewModelScope.launch {
            val response=repository.getLanguage()
            mutableLanguageLiveData.value=response
        }
    }
    fun insertLanguage(languageRoom: LanguageRoom){
        viewModelScope.launch {
            repository.insertLanguage(languageRoom)
        }
        getLanguage()
    }
    fun deleteLanguages(){
        viewModelScope.launch {
            repository.deleteAllLanguage()
        }
        getLanguage()
    }

}