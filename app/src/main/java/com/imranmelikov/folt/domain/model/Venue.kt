package com.imranmelikov.folt.domain.model

import java.io.Serializable

data class Venue(var id:String,val venueName:String,val venueText:String,val imageUrl:String,val venueLogoUrl:String,val delivery: Delivery,
                 val location: Location,val venueInformation: VenueInformation,
                 val venuePopularity: VenuePopularity,val bribe:Boolean,val type:String,val serviceFee:Number,val parentVenueId:String,
                 var restaurant:Boolean):Serializable