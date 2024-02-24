package com.imranmelikov.folt.domain.model

import java.io.Serializable

data class Order(val id:String,val email:String,val userName:String,val userId:String,val tel:String,
    val deliveryTime: String,val itemSubtotal:Number,val contactDelivery:String,val totalPrice:Number,
    val venueDetailsList: List<VenueDetails>,val address: Address,val history:String):Serializable