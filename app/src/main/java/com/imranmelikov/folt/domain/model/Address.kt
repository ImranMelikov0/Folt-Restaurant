package com.imranmelikov.folt.domain.model

import java.io.Serializable

data class Address(var id:String,val addressName:String,var countryName:String,var buildingName:String,
    var entrance:Number,var floor:Number,var apartment:Number,var doorCode:String,var selected:Boolean):Serializable
