package com.imranmelikov.folt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imranmelikov.folt.data.model.VenueDetailsRoom
import com.imranmelikov.folt.domain.model.VenueDetails

@Database([VenueDetailsRoom::class], version = 1)
abstract class FoltDataBase:RoomDatabase() {
    abstract fun foltDao():FoltDao
}