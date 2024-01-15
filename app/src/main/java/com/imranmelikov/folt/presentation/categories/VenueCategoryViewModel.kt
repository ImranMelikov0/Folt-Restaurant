package com.imranmelikov.folt.presentation.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.domain.model.VenueCategory

class VenueCategoryViewModel:ViewModel() {
    private val mutableRestaurantCategoryList= MutableLiveData<List<VenueCategory>>()
    val restaurantCategoryLiveData: LiveData<List<VenueCategory>>
        get() = mutableRestaurantCategoryList
    private val mutableStoreCategoryList= MutableLiveData<List<VenueCategory>>()
    val storeCategoryLiveData: LiveData<List<VenueCategory>>
        get() = mutableStoreCategoryList

    fun getRestaurantCategories(){
        val venueCategory=VenueCategory(1,"","American")
        val venueCategory2=VenueCategory(2,"","America")
        val venueCategory3=VenueCategory(3,"","Americ")
        val venueCategoryList= listOf(venueCategory,venueCategory2,venueCategory3)
        mutableRestaurantCategoryList.value=venueCategoryList
    }
    fun getStoreCategories(){
        val venueCategory=VenueCategory(1,"","Sweets")
        val venueCategory2=VenueCategory(2,"","Sweet")
        val venueCategory3=VenueCategory(3,"","Swee")
        val venueCategoryList= listOf(venueCategory,venueCategory2,venueCategory3)
        mutableStoreCategoryList.value=venueCategoryList
    }
}