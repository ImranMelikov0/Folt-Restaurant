package com.imranmelikov.folt.domain.model

import java.io.Serializable

data class User(var id:String,var email:String,var password:String,var tel:String,var imageUrl:String,
                var firstName:String,var lastName:String,var country: Country,var language: Language,var location: Location):Serializable
