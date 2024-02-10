package com.imranmelikov.folt.presentation.storeitems

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.domain.model.VenueDetails
import com.imranmelikov.folt.domain.model.VenueDetailsItem

class StoreItemsViewModel:ViewModel() {
    private val storeMenuMutableList= MutableLiveData<List<VenueDetailsItem>>()
    val storeMenuLiveData: LiveData<List<VenueDetailsItem>>
        get() = storeMenuMutableList

    fun getStoreMenuList(){
        val storeMenu=VenueDetails(1,"",5.00,"menu","faksdjfja",false,4,0,false)
        val storeMenu2=VenueDetails(2,"",4.00,"menu","faksdjfja",false,4,0,false)
        val storeMenu3=VenueDetails(3,"",7.00,"menu","faksdjfja",false,4,0,false)
        val menuList= listOf(storeMenu,storeMenu2,storeMenu3)
        val menuList2= listOf(storeMenu2,storeMenu2,storeMenu2)
        val menuList3= listOf(storeMenu,storeMenu,storeMenu)
        val venueDetails= VenueDetailsItem(1,"title",menuList,3)
        val venueDetails2= VenueDetailsItem(2,"title2",menuList2,3)
        val venueDetails3= VenueDetailsItem(3,"title3",menuList3,3)
        val venueDetailList= listOf(venueDetails,venueDetails2,venueDetails3)
        storeMenuMutableList.value=venueDetailList
    }
}