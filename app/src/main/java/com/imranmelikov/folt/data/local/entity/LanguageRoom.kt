package com.imranmelikov.folt.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LanguageRoom(val language:String,val languageCode:String){
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}