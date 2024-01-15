package com.imranmelikov.folt.presentation.stores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.domain.model.Delivery
import com.imranmelikov.folt.domain.model.Location
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueInformation
import com.imranmelikov.folt.domain.model.VenuePopularity

class StoreViewModel:ViewModel() {
   private val mutableVenueList=MutableLiveData<List<Venue>>()
    val venueLiveData:LiveData<List<Venue>>
        get() = mutableVenueList

    fun getVenues(){
        val delivery=Delivery("0.00","30-40 min")
        val delivery2=Delivery("2.00","30-40 min")
        val location=Location(2,2)
        val venueInformation=VenueInformation("address","239023920","www",true)
        val venuePopularity=VenuePopularity(9.00,false,false,false)
        val venuePopularity2=VenuePopularity(7.00,false,false,false)
        val venuePopularity3=VenuePopularity(4.00,false,false,false)
        val venue=Venue(1,"Stores","Venue","",delivery,location,venueInformation,venuePopularity,false,"Sweets",2.00,2)
        val venue2=Venue(2,"Stores","Venue","",delivery2,location,venueInformation,venuePopularity2,false,"Sweet",2.00,2)
        val venue3=Venue(3,"Stores","Venue","",delivery,location,venueInformation,venuePopularity3,false,"Swee",2.00,2)
        val venueList= listOf(venue,venue2,venue3,venue)
        mutableVenueList.value = venueList
    }
}