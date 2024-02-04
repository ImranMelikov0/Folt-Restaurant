package com.imranmelikov.folt.domain.model

data class DiscoveryItem(val title:String,val viewType:Int) {
    var offerList:List<Offer>?=null
    var parentVenueList:List<ParentVenue>?=null
    var venueList:List<Venue>?=null
    var venueCategoryList:List<VenueCategory>?=null

    constructor(title: String,viewType: Int,offerList: List<Offer>,venueList: List<Venue>,dummy: Int): this(title,viewType){
        this.offerList=offerList
        this.venueList=venueList
    }
    constructor(title: String,viewType: Int,parentVenueList:List<ParentVenue>,venueList: List<Venue>):this(title,viewType){
        this.parentVenueList=parentVenueList
        this.venueList=venueList
    }
    constructor(title: String,viewType: Int,venueList: List<Venue>):this(title,viewType){
        this.venueList=venueList
    }
    constructor(title: String,viewType: Int,venueCategoryList: List<VenueCategory>,venueList: List<Venue>,dummy:Boolean):this(title,viewType){
        this.venueCategoryList=venueCategoryList
        this.venueList=venueList
    }
}