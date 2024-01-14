package com.imranmelikov.folt.presentation.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory

class VenueCategoryViewModel:ViewModel() {
    private val mutableVenueCategoryList= MutableLiveData<List<VenueCategory>>()
    val venueCategoryLiveData: LiveData<List<VenueCategory>>
        get() = mutableVenueCategoryList


    fun getVenueCategories(){
        val venueCategory=VenueCategory(1,"","American")
        val venueCategory2=VenueCategory(2,"","America")
        val venueCategory3=VenueCategory(3,"","Americ")
        val venueCategoryList= listOf(venueCategory,venueCategory2,venueCategory3)
        mutableVenueCategoryList.value=venueCategoryList
    }
}