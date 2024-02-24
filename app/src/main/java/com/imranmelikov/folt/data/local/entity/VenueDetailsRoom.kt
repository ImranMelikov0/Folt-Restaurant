package com.imranmelikov.folt.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VenueDetailsRoom(var id:String, val imageUrl:String, var price:Double, val menuName:String, val about:String,
                            var popularity:Boolean, var stock:Int,
                            var count:Int, var selected:Boolean){
    @PrimaryKey(autoGenerate = true)
    var idRoom:Int?=null
}