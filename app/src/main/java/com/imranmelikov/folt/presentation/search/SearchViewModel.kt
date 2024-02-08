package com.imranmelikov.folt.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.domain.model.Delivery
import com.imranmelikov.folt.domain.model.Location
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueInformation
import com.imranmelikov.folt.domain.model.VenuePopularity

class SearchViewModel:ViewModel() {
    private val mutableVenueList= MutableLiveData<List<Venue>>()
    val venueLiveData: LiveData<List<Venue>>
        get() = mutableVenueList

    fun getVenues(searchText:String){
        val delivery= Delivery("0.00","30")
        val delivery2= Delivery("2.00","20")
        val location= Location(2,2)
        val venueInformation= VenueInformation("address","239023920","www",true)
        val venuePopularity= VenuePopularity(9.00,true,true,false)
        val venuePopularity2= VenuePopularity(7.00,false,true,false)
        val venuePopularity3= VenuePopularity(4.00,false,false,false)
        val venue= Venue(1,"Restaurant","Venue","","",delivery,location,venueInformation,venuePopularity,false,"American",2.00,1,true)
        val venue2= Venue(2,"Restaurant","Venue2","","",delivery2,location,venueInformation,venuePopularity2,false,"American",2.00,2,true)
        val venue3= Venue(3,"Restauran","Venue3","","",delivery,location,venueInformation,venuePopularity3,false,"Americ",2.00,3,true)
        val venuePopularity4=VenuePopularity(9.00,true,false,false)
        val venuePopularity5=VenuePopularity(7.00,false,false,false)
        val venuePopularity6=VenuePopularity(4.00,false,false,false)
        val venue4=Venue(4,"Stores","Venue","","",delivery,location,venueInformation,venuePopularity4,false,"Sweets",2.00,4,false)
        val venue5=Venue(5,"Stores","Venue2","","",delivery2,location,venueInformation,venuePopularity5,false,"Sweet",2.00,5,false)
        val venue6=Venue(6,"Store","Venue3","","",delivery,location,venueInformation,venuePopularity6,false,"Swee",2.00,6,false)
        //filter open venue
        val venueList= listOf(venue,venue2,venue3,venue4,venue5,venue6)
        val filterList=venueList.filter { it.venueName==searchText }
        mutableVenueList.value = venueList
    }
}