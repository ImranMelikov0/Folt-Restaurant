package com.imranmelikov.folt.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.imranmelikov.folt.data.local.entity.VenueDetailsRoom

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
}