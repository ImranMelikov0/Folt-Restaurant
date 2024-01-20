package com.imranmelikov.folt.domain.model

data class VenueDetails(var id:Int,val image:String,val price:Double,val menuName:String,val about:String,
                        val parentId:Int,var popularity:Boolean,val stock:Int,
                        val count:Int,var selected:Boolean)