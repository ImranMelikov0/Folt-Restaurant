package com.imranmelikov.folt.domain.repository

import com.imranmelikov.folt.data.model.VenueDetailsRoom
import com.imranmelikov.folt.domain.model.Banner
import com.imranmelikov.folt.domain.model.CRUD
import com.imranmelikov.folt.domain.model.Offer
import com.imranmelikov.folt.domain.model.ParentVenue
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory
import com.imranmelikov.folt.domain.model.VenueDetails
import com.imranmelikov.folt.domain.model.VenueDetailsItem
import com.imranmelikov.folt.util.Resource

interface FoltRepository {
    suspend fun getOffer():Resource<List<Offer>>

    suspend fun getBanner():Resource<List<Banner>>

    suspend fun getParentVenue():Resource<List<ParentVenue>>

    suspend fun getVenue():Resource<List<Venue>>

    suspend fun getVenueCategory():Resource<List<VenueCategory>>

    suspend fun getVenueDetailsItemVenue():Resource<List<VenueDetailsItem>>

    suspend fun getVenueDetailsItemStore():Resource<List<VenueDetailsItem>>

    suspend fun getFavoriteVenue(userId:String):Resource<List<Venue>>

    suspend fun searchVenue(searchText: String):Resource<List<Venue>>

    suspend fun searchVenueDetailsItemStore(searchText: String):Resource<List<VenueDetailsItem>>

    suspend fun searchVenueDetailsItemVenue(searchText: String):Resource<List<VenueDetailsItem>>

    suspend fun insertFavoriteVenue(venue: Venue,userId: String):Resource<CRUD>

    suspend fun deleteFavoriteVenue(documentId:String,userId: String):Resource<CRUD>

    suspend fun updateVenueDetailsStock(documentId:String,venueDetailList: List<VenueDetails>):Resource<CRUD>

    suspend fun insertVenueDetailsToRoom(venueDetailsRoom: VenueDetailsRoom)

    suspend fun deleteVenueDetailsFromRoom(venueDetailsRoom: VenueDetailsRoom)

    suspend fun updateVenueDetailsFromRoom(venueDetailsRoom: VenueDetailsRoom)

    suspend fun getVenueDetailsFromRoom():List<VenueDetailsRoom>

    suspend fun deleteAllVenueDetailsFromRoom()
}