package com.imranmelikov.folt.domain.model

data class VenueDetailsItem (val title: String){
    var venueDetailList:List<VenueDetails>?=null
    var storeMenuCategory:List<StoreMenuCategory>?=null
    var id:Int?=null
    var parentId:Int?=null

    constructor(id:Int,title: String,venueDetailList: List<VenueDetails>,parentId:Int):this(title){
        this.venueDetailList=venueDetailList
        this.id=id
        this.parentId=parentId
    }
    constructor(id:Int,title: String,storeMenuCategory: List<StoreMenuCategory>,parentId: Int,dummy:Boolean):this(title){
        this.id=id
        this.parentId=parentId
        this.storeMenuCategory=storeMenuCategory
    }
}