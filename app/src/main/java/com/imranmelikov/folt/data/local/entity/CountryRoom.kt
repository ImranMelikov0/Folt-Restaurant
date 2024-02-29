package com.imranmelikov.folt.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountryRoom(val countryName:String,val capital:String){
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}