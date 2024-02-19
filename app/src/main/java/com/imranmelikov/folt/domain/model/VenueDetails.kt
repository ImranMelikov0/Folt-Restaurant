package com.imranmelikov.folt.domain.model

data class VenueDetails(var id:String,val imageUrl:String,var price:Number,val menuName:String,val about:String,
                        var popularity:Boolean,var stock:Number,
                        val count:Number,var selected:Boolean)