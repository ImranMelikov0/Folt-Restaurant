package com.imranmelikov.folt.domain.model

data class VenueDetailsItem (val title: String){
    var venueDetailList:List<VenueDetails>?=null
    var storeMenuCategory:List<StoreMenuCategory>?=null

    constructor(title: String,venueDetailList: List<VenueDetails>):this(title){
        this.venueDetailList=venueDetailList
    }
    constructor(title: String,storeMenuCategory: List<StoreMenuCategory>,dummy:Boolean):this(title){
        this.storeMenuCategory=storeMenuCategory
    }
}