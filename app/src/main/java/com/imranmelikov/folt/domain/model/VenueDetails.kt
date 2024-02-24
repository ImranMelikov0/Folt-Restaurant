package com.imranmelikov.folt.domain.model

import java.io.Serializable

data class VenueDetails(var id:String, val imageUrl:String, var price:Number, val menuName:String, val about:String,
                        var popularity:Boolean, var stock:Number,
                        var count:Number, var selected:Boolean):Serializable