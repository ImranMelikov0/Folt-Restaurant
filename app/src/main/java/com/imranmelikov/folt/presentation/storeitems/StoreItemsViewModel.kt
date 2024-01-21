package com.imranmelikov.folt.presentation.storeitems

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.domain.model.VenueDetails

class StoreItemsViewModel:ViewModel() {
    private val storeMenuMutableList= MutableLiveData<List<VenueDetails>>()
    val storeMenuLiveData: LiveData<List<VenueDetails>>
        get() = storeMenuMutableList

    fun getStoreMenuList(){
        val storeMenu=VenueDetails(1,"",4.00,"menu","faksdjfja",1,false,4,0,false)
        val storeMenu2=VenueDetails(2,"",4.00,"menu","faksdjfja",3,false,4,0,false)
        val storeMenu3=VenueDetails(3,"",4.00,"menu","faksdjfja",3,false,4,0,false)
        val menuList= listOf(storeMenu,storeMenu2,storeMenu3)
        storeMenuMutableList.value=menuList
    }
}