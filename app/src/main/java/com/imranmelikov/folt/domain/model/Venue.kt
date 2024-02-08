package com.imranmelikov.folt.domain.model

import java.io.Serializable

data class Venue(var id:Int,val venueName:String,val venueText:String,val image:String,val venueLogo:String,val delivery: Delivery,
                 val location: Location,val venueInformation: VenueInformation,
                 val venuePopularity: VenuePopularity,val bribe:Boolean,val type:String,val serviceFee:Double,val parentVenueId:Int,
                 var restaurant:Boolean):Serializable