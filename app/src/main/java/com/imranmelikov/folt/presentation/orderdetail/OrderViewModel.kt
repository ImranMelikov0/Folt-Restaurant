package com.imranmelikov.folt.presentation.orderdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.domain.model.CRUD
import com.imranmelikov.folt.domain.model.Order
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {

    private val mutableMessageLiveData= MutableLiveData<Resource<CRUD>>()
    val msgLiveData: LiveData<Resource<CRUD>>
        get() = mutableMessageLiveData

    private val exceptionHandlerMessage = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableMessageLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    private val mutableOrderListLiveData=MutableLiveData<Resource<List<Order>>>()
    val orderListLiveData:LiveData<Resource<List<Order>>>
        get() = mutableOrderListLiveData

    private val exceptionHandlerOrder = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableOrderListLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    fun getOrderList(){
        mutableOrderListLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerOrder){
            val response=repository.getOrder()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerOrder){
                response.data?.let {
                    mutableOrderListLiveData.value=Resource.success(it)
                }
            }
        }
    }
    fun insertOrder(order: Order){
        mutableMessageLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerMessage){
            val response=repository.insertOrder(order)
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerMessage){
                response.data?.let {
                    mutableMessageLiveData.value=Resource.success(it)
                }
            }
        }
        getOrderList()
    }
}