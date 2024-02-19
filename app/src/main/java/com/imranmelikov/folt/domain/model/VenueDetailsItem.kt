package com.imranmelikov.folt.domain.model

data class VenueDetailsItem (val title: String){
    var venueDetailList:List<VenueDetails>?=null
    var storeMenuCategory:List<StoreMenuCategory>?=null
    var id:String?=null
    var parentId:String?=null

    constructor(id:String,title: String,venueDetailList: List<VenueDetails>,parentId:String):this(title){
        this.venueDetailList=venueDetailList
        this.id=id
        this.parentId=parentId
    }
    constructor(id:String,title: String,storeMenuCategory: List<StoreMenuCategory>,parentId: String,dummy:Boolean):this(title){
        this.id=id
        this.parentId=parentId
        this.storeMenuCategory=storeMenuCategory
    }
}