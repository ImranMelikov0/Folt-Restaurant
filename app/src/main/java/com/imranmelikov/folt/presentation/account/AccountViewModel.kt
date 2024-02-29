package com.imranmelikov.folt.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.domain.model.CRUD
import com.imranmelikov.folt.domain.model.User
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {
    private val mutableMessageLiveData= MutableLiveData<Resource<CRUD>>()
    val msgLiveData: LiveData<Resource<CRUD>>
        get() = mutableMessageLiveData

    private val exceptionHandlerMessage = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableMessageLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    private val mutableUserLiveData=MutableLiveData<Resource<User>>()
    val userLiveData:LiveData<Resource<User>>
        get() = mutableUserLiveData

    private val exceptionHandlerUser= CoroutineExceptionHandler{_,throwable->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableUserLiveData.value=Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    fun getUser(){
        mutableUserLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerUser){
           val response= repository.getUser()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerUser){
                response.data?.let {
                    mutableUserLiveData.value=Resource.success(it)
                }
            }
        }
    }
    fun updateUser(user: User){
        mutableMessageLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerMessage){
            val response=repository.updateUser(user)
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerMessage){
                response.data?.let {
                   mutableMessageLiveData.value= Resource.success(it)
                }
            }
        }
        getUser()
    }
    fun deleteUser(){
        mutableMessageLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerMessage){
            val response=repository.deleteUser()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerMessage){
                response.data?.let {
                  mutableMessageLiveData.value= Resource.success(it)
                }
            }
        }
    }
    fun deleteUserFromFireStore(user: User){
        mutableMessageLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerMessage){
            val response=repository.deleteUserFromFreStore(user)
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerMessage){
                response.data?.let {
                    mutableMessageLiveData.value= Resource.success(it)
                }
            }
        }
    }
}