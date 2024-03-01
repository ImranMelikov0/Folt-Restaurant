package com.imranmelikov.folt.domain.model

import java.io.Serializable

data class Order(var id:String,val venueName:String,val email:String,val firstName:String,val lastName:String,val userId:String,val tel:String,
    val deliveryTime: String,val itemSubtotal:String,val contactDelivery:String,val totalPrice:String,
    val venueDetailsList: List<VenueDetails>,val address: Address,val history:String):Serializable