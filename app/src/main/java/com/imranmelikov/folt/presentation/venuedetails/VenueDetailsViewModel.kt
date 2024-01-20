package com.imranmelikov.folt.presentation.venuedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.domain.model.RestaurantMenuCategory
import com.imranmelikov.folt.domain.model.StoreMenuCategory
import com.imranmelikov.folt.domain.model.VenueDetails

class VenueDetailsViewModel:ViewModel() {
    private val restaurantMenuCategoryMutableList=MutableLiveData<List<RestaurantMenuCategory>>()
    val restaurantMenuCategoryLiveData:LiveData<List<RestaurantMenuCategory>>
    get() = restaurantMenuCategoryMutableList

    private val storeMenuCategoryMutableList=MutableLiveData<List<StoreMenuCategory>>()
    val storeMenuCategoryLiveData:LiveData<List<StoreMenuCategory>>
        get() = storeMenuCategoryMutableList

    private val restaurantMenuMutableList=MutableLiveData<List<VenueDetails>>()
    val restaurantMenuLiveData:LiveData<List<VenueDetails>>
        get() = restaurantMenuMutableList

    private val storeMenuMutableList=MutableLiveData<List<VenueDetails>>()
    val storeMenuLiveData:LiveData<List<VenueDetails>>
        get() = storeMenuMutableList

    fun getRestaurantMenuCategoryList(){
        val restaurantMenuCategory=RestaurantMenuCategory(1,"title",2)
        val restaurantMenuCategory2=RestaurantMenuCategory(2,"title2",1)
        val restaurantMenuCategory3=RestaurantMenuCategory(3,"title",3)
        val categoryList= listOf(restaurantMenuCategory,restaurantMenuCategory2,restaurantMenuCategory3)
        restaurantMenuCategoryMutableList.value=categoryList
    }
    fun getRestaurantMenuList(){
        val restaurantMenu=VenueDetails(1,"",5.00,"menu","faksdjfja",2,false,4,0,false)
        val restaurantMenu2=VenueDetails(2,"",4.00,"menu","faksdjfja",3,false,4,0,false)
        val restaurantMenu3=VenueDetails(3,"",7.00,"menu","faksdjfja",3,false,4,0,false)
        val menuList= listOf(restaurantMenu,restaurantMenu2,restaurantMenu3)
        restaurantMenuMutableList.value=menuList
    }
    fun getStoreMenuCategoryList(){
        val restaurantMenuCategory=RestaurantMenuCategory(1,"store",1)
        val restaurantMenuCategory2=RestaurantMenuCategory(2,"store",3)
        val restaurantMenuCategory3=RestaurantMenuCategory(3,"store",3)
        val storeMenuCategory=StoreMenuCategory(restaurantMenuCategory,"")
        val storeMenuCategory2=StoreMenuCategory(restaurantMenuCategory2,"")
        val storeMenuCategory3=StoreMenuCategory(restaurantMenuCategory3,"")
        val storeMenuCategoryList= listOf(storeMenuCategory,storeMenuCategory2,storeMenuCategory3)
        storeMenuCategoryMutableList.value=storeMenuCategoryList
    }
//    fun getStoreMenuList(){
//        val storeMenu=VenueDetails(1,"",4.00,"menu","faksdjfja",1,false,4,0,false)
//        val storeMenu2=VenueDetails(2,"",4.00,"menu","faksdjfja",3,false,4,0,false)
//        val storeMenu3=VenueDetails(3,"",4.00,"menu","faksdjfja",2,false,4,0,false)
//        val menuList= listOf(storeMenu,storeMenu2,storeMenu3)
//         storeMenuMutableList.value=menuList
//    }
}