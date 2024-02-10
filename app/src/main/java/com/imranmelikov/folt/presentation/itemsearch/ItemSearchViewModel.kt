package com.imranmelikov.folt.presentation.itemsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.constants.StoreCategoryTitle
import com.imranmelikov.folt.domain.model.RestaurantMenuCategory
import com.imranmelikov.folt.domain.model.StoreMenuCategory
import com.imranmelikov.folt.domain.model.VenueDetails
import com.imranmelikov.folt.domain.model.VenueDetailsItem

class ItemSearchViewModel:ViewModel() {

    private val restaurantMenuMutableList= MutableLiveData<List<VenueDetailsItem>>()
    val restaurantMenuLiveData: LiveData<List<VenueDetailsItem>>
        get() = restaurantMenuMutableList

    private val storeMenuMutableList= MutableLiveData<List<VenueDetailsItem>>()
    val storeMenuLiveData: LiveData<List<VenueDetailsItem>>
        get() = storeMenuMutableList


    private val storeMenuCategoryMutableList=MutableLiveData<List<VenueDetailsItem>>()
    val storeMenuCategoryLiveData:LiveData<List<VenueDetailsItem>>
        get() = storeMenuCategoryMutableList

    fun getRestaurantMenuList(searchString: String){
        val restaurantMenu=VenueDetails(1,"",5.00,"menu","faksdjfja",false,4,0,false)
        val restaurantMenu2=VenueDetails(2,"",4.00,"menu","faksdjfja",false,4,0,false)
        val restaurantMenu3=VenueDetails(3,"",7.00,"menu","faksdjfja",false,4,0,false)
        val menuList= listOf(restaurantMenu,restaurantMenu2,restaurantMenu3)
        val menuList2= listOf(restaurantMenu2,restaurantMenu2,restaurantMenu2)
        val menuList3= listOf(restaurantMenu,restaurantMenu,restaurantMenu)
        val venueDetails= VenueDetailsItem(1,"title",menuList,3)
        val venueDetails2= VenueDetailsItem(2,"title2",menuList2,3)
        val venueDetails3= VenueDetailsItem(3,"title3",menuList3,3)
        
        val venueDetailList= listOf(venueDetails,venueDetails2,venueDetails3)
        val filteredList=venueDetailList.filter { it.title==searchString }
        restaurantMenuMutableList.value=venueDetailList
    }

    fun getStoreMenuList(searchString: String){
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
        val filteredList=venueDetailList.filter { it.title==searchString }
        storeMenuMutableList.value=venueDetailList
    }

    fun getStoreMenuCategoryList(searchString: String){
        val storeMenuCategory=StoreMenuCategory(1,"store","")
        val storeMenuCategory2=StoreMenuCategory(2,"store2","")
        val storeMenuCategory3=StoreMenuCategory(3,"store3","")
        val storeMenuCategoryList= listOf(storeMenuCategory,storeMenuCategory2,storeMenuCategory3)
        val venueDetailsItem=VenueDetailsItem(1, StoreCategoryTitle.storeCategoryTitle,storeMenuCategoryList,5,false)
        val venueDetailsItemList= listOf(venueDetailsItem)
        val filteredList=venueDetailsItemList.map { it.storeMenuCategory?.filter { it.title==searchString } }
        storeMenuCategoryMutableList.value=venueDetailsItemList
    }
}