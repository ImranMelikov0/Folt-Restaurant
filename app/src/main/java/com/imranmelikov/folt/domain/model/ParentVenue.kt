package com.imranmelikov.folt.domain.model

import java.io.Serializable

data class ParentVenue(var id:String,val venueName:String,val imageUrl:String,val popularity:Boolean,val bribe:Boolean,var restaurant:Boolean):Serializable