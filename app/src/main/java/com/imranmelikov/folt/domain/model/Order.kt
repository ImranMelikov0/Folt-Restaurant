package com.imranmelikov.folt.domain.model

data class Order(val id:String,val email:String,val userName:String,val userId:String,val tel:String,
    val deliveryTime: String,val itemSubtotal:Number,val contactDelivery:String,val totalPrice:Number,
    val venueDetailsList: List<VenueDetails>,val paymentType:String,val address: Address,val history:String)