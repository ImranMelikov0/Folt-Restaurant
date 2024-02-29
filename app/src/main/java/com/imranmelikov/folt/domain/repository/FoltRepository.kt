package com.imranmelikov.folt.domain.repository

import com.imranmelikov.folt.data.local.entity.CountryRoom
import com.imranmelikov.folt.data.local.entity.LanguageRoom
import com.imranmelikov.folt.data.remote.dto.CountryDto
import com.imranmelikov.folt.data.local.entity.VenueDetailsRoom
import com.imranmelikov.folt.domain.model.Address
import com.imranmelikov.folt.domain.model.Banner
import com.imranmelikov.folt.domain.model.CRUD
import com.imranmelikov.folt.domain.model.Language
import com.imranmelikov.folt.domain.model.Offer
import com.imranmelikov.folt.domain.model.ParentVenue
import com.imranmelikov.folt.domain.model.User
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory
import com.imranmelikov.folt.domain.model.VenueDetails
import com.imranmelikov.folt.domain.model.VenueDetailsItem
import com.imranmelikov.folt.util.Resource
import retrofit2.Response

interface FoltRepository {
    suspend fun getOffer():Resource<List<Offer>>

    suspend fun getBanner():Resource<List<Banner>>

    suspend fun getParentVenue():Resource<List<ParentVenue>>

    suspend fun getVenue():Resource<List<Venue>>

    suspend fun getVenueCategory():Resource<List<VenueCategory>>

    suspend fun getVenueDetailsItemVenue():Resource<List<VenueDetailsItem>>

    suspend fun getVenueDetailsItemStore():Resource<List<VenueDetailsItem>>

    suspend fun getFavoriteVenue():Resource<List<Venue>>

    suspend fun searchVenue(searchText: String):Resource<List<Venue>>

    suspend fun searchVenueDetailsItemStore(searchText: String):Resource<List<VenueDetailsItem>>

    suspend fun searchVenueDetailsItemVenue(searchText: String):Resource<List<VenueDetailsItem>>

    suspend fun insertFavoriteVenue(venue: Venue):Resource<CRUD>

    suspend fun deleteFavoriteVenue(documentId:String):Resource<CRUD>

    suspend fun updateVenueDetailsStock(documentId:String,venueDetailList: List<VenueDetails>):Resource<CRUD>

    suspend fun insertVenueDetailsToRoom(venueDetailsRoom: VenueDetailsRoom)

    suspend fun deleteVenueDetailsFromRoom(venueDetailsRoom: VenueDetailsRoom)

    suspend fun updateVenueDetailsFromRoom(venueDetailsRoom: VenueDetailsRoom)

    suspend fun getVenueDetailsFromRoom():List<VenueDetailsRoom>

    suspend fun deleteAllVenueDetailsFromRoom()

    suspend fun getCountries():Response<CountryDto>

    suspend fun getAddress():Resource<List<Address>>

    suspend fun insertAddress(address: Address):Resource<CRUD>

    suspend fun deleteAddress(documentId: String):Resource<CRUD>

    suspend fun updateAddress(documentId: String,address: Address):Resource<CRUD>

    suspend fun signUpUser(user:User):Resource<CRUD>

    suspend fun signInUser(email:String,password:String):Resource<CRUD>

    suspend fun updateUser(user:User):Resource<CRUD>

    suspend fun deleteUser():Resource<CRUD>

    suspend fun deleteUserFromFreStore(user: User):Resource<CRUD>

    suspend fun getUser():Resource<User>

    suspend fun insertCountry(countryRoom: CountryRoom)

    suspend fun deleteAllCountry()

    suspend fun getCountry():List<CountryRoom>

    suspend fun insertLanguage(languageRoom: LanguageRoom)

    suspend fun deleteAllLanguage()

    suspend fun getLanguage():List<LanguageRoom>

    suspend fun getLanguages():Resource<List<Language>>

}