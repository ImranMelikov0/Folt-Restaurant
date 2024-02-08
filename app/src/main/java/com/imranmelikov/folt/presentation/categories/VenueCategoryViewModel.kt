package com.imranmelikov.folt.presentation.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.domain.model.VenueCategory

class VenueCategoryViewModel:ViewModel() {
    private val mutableVenueCategoryList= MutableLiveData<List<VenueCategory>>()
    val venueCategoryLiveData: LiveData<List<VenueCategory>>
        get() = mutableVenueCategoryList

    fun getVenueCategories(){
        val venueCategory=VenueCategory(1,"","American",true)
        val venueCategory2=VenueCategory(2,"","America",true)
        val venueCategory3=VenueCategory(3,"","Americ",true)
        val venueCategory4=VenueCategory(1,"","Sweets",false)
        val venueCategory5=VenueCategory(2,"","Sweet",false)
        val venueCategory6=VenueCategory(3,"","Swee",false)
        val venueCategoryList= listOf(venueCategory,venueCategory2,venueCategory3,venueCategory4,venueCategory5,venueCategory6)
        mutableVenueCategoryList.value=venueCategoryList
    }
}