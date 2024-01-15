package com.imranmelikov.folt.domain.model

data class Venue(var id:Int,val venueName:String,val venueText:String,val image:String,val delivery: Delivery,
                 val location: Location,val venueInformation: VenueInformation,
                 val venuePopularity: VenuePopularity,val bribe:Boolean,val type:String,val serviceFee:Double,val parentVenueId:Int)