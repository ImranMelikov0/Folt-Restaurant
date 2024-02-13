package com.imranmelikov.folt.domain.model

data class VenueDetails(var id:Int,val image:String,var price:Double,val menuName:String,val about:String,
                        var popularity:Boolean,var stock:Int,
                        val count:Int,var selected:Boolean)