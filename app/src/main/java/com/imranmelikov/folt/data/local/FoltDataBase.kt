package com.imranmelikov.folt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imranmelikov.folt.data.local.entity.CountryRoom
import com.imranmelikov.folt.data.local.entity.LanguageRoom
import com.imranmelikov.folt.data.local.entity.VenueDetailsRoom

@Database([VenueDetailsRoom::class,CountryRoom::class,LanguageRoom::class],version = 2)
abstract class FoltDataBase:RoomDatabase() {
    abstract fun foltDao():FoltDao
}