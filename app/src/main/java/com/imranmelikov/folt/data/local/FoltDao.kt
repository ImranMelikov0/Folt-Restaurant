package com.imranmelikov.folt.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.imranmelikov.folt.data.local.entity.CountryRoom
import com.imranmelikov.folt.data.local.entity.LanguageRoom
import com.imranmelikov.folt.data.local.entity.VenueDetailsRoom
import com.imranmelikov.folt.domain.model.Language

@Dao
interface FoltDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenueDetail(venueDetailsRoom: VenueDetailsRoom)

    @Delete
    suspend fun deleteVenueDetail(venueDetailsRoom: VenueDetailsRoom)

    @Update
    suspend fun updateVenueDetail(venueDetailsRoom: VenueDetailsRoom)

    @Query("SELECT * FROM VenueDetailsRoom")
    suspend fun getVenueDetails():List<VenueDetailsRoom>

    @Query("DELETE FROM VenueDetailsRoom")
    suspend fun deleteAllVenueDetails()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(countryRoom: CountryRoom)

    @Query("DELETE FROM CountryRoom")
    suspend fun deleteAllCountry()

    @Query("SELECT * FROM CountryRoom")
    suspend fun getCountry():List<CountryRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLanguage(languageRoom: LanguageRoom)

    @Query("DELETE FROM LanguageRoom")
    suspend fun deleteAllLanguage()

    @Query("SELECT * FROM LanguageRoom")
    suspend fun getLanguage():List<LanguageRoom>
}