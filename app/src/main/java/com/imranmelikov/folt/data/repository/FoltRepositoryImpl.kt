package com.imranmelikov.folt.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.imranmelikov.folt.constants.FireStoreConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.FireStoreCollectionConstants
import com.imranmelikov.folt.data.local.FoltDao
import com.imranmelikov.folt.data.remote.dto.CountryDto
import com.imranmelikov.folt.data.local.entity.VenueDetailsRoom
import com.imranmelikov.folt.data.remote.webservice.CountryApi
import com.imranmelikov.folt.domain.model.Address
import com.imranmelikov.folt.domain.model.Banner
import com.imranmelikov.folt.domain.model.CRUD
import com.imranmelikov.folt.domain.model.Delivery
import com.imranmelikov.folt.domain.model.Location
import com.imranmelikov.folt.domain.model.Offer
import com.imranmelikov.folt.domain.model.ParentVenue
import com.imranmelikov.folt.domain.model.StoreMenuCategory
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory
import com.imranmelikov.folt.domain.model.VenueDetails
import com.imranmelikov.folt.domain.model.VenueDetailsItem
import com.imranmelikov.folt.domain.model.VenueInformation
import com.imranmelikov.folt.domain.model.VenuePopularity
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FoltRepositoryImpl(private val fireStore: FirebaseFirestore,private val dao: FoltDao,private val countryApi: CountryApi):FoltRepository {
override suspend fun getOffer(): Resource<List<Offer>> {
    return try {
        suspendCoroutine { continuation ->
            val offerList = mutableListOf<Offer>()
            fireStore.collection(FireStoreCollectionConstants.offer).get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty){
                    continuation.resume(Resource.success(emptyList()))
                }else {
                    for (document in querySnapshot.documents) {
                        val bannerMap = document[FireStoreConstants.banner] as HashMap<*, *>
                        val banner = Banner(
                            id = bannerMap[FireStoreConstants.id] as String,
                            imageUrl = bannerMap[FireStoreConstants.imageUrl] as String,
                            title = bannerMap[FireStoreConstants.title] as String,
                            text = bannerMap[FireStoreConstants.text] as String,
                            bribe = bannerMap[FireStoreConstants.bribe] as Boolean
                        )
                        banner.id = document.id
                        val parentVenue = document.getString(FireStoreConstants.parentVenue) ?: ""
                        val offer = Offer(banner, parentVenue)
                        offerList.add(offer)
                    }
                    continuation.resume(Resource.success(offerList))
                }
            }.addOnFailureListener { e ->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null))
                }
        }
    } catch (e: Exception) {
        Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null)
    }
}

    override suspend fun getBanner(): Resource<List<Banner>> {
        return try {
            suspendCoroutine { continuation ->
                val bannerList= mutableListOf<Banner>()
                fireStore.collection(FireStoreCollectionConstants.banner).get().addOnSuccessListener {querySnapshot ->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else {
                        for (document in querySnapshot.documents) {
                            val banner = Banner(
                                id = document.getString(FireStoreConstants.id) as String,
                                imageUrl = document.getString(FireStoreConstants.imageUrl) as String,
                                title = document.getString(FireStoreConstants.title) as String,
                                text = document.getString(FireStoreConstants.text) as String,
                                bribe = document.getBoolean(FireStoreConstants.bribe) as Boolean
                            )
                            banner.id = document.id
                            bannerList.add(banner)
                        }
                        continuation.resume(Resource.success(bannerList))
                    }
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        } catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun getParentVenue(): Resource<List<ParentVenue>> {
        return try {
            suspendCoroutine { continuation ->
                val parentVenueList= mutableListOf<ParentVenue>()
                fireStore.collection(FireStoreCollectionConstants.ParentVenue).get().addOnSuccessListener {querySnapshot->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else {
                        for (document in querySnapshot.documents) {
                            val parentVenue = ParentVenue(
                                id = document.getString(FireStoreConstants.id) as String,
                                venueName = document.getString(FireStoreConstants.venueName) as String,
                                bribe = document.getBoolean(FireStoreConstants.bribe) as Boolean,
                                imageUrl = document.getString(FireStoreConstants.imageUrl) as String,
                                popularity = document.getBoolean(FireStoreConstants.popularity) as Boolean,
                                restaurant = document.getBoolean(FireStoreConstants.restaurant) as Boolean
                            )
                            parentVenue.id = document.id
                            parentVenueList.add(parentVenue)
                        }
                        continuation.resume(Resource.success(parentVenueList))
                    }
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun getVenue(): Resource<List<Venue>> {
        return try {
            suspendCoroutine { continuation ->
                val venueList= mutableListOf<Venue>()
                fireStore.collection(FireStoreCollectionConstants.venue).get().addOnSuccessListener {querySnapshot->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else {
                        for (document in querySnapshot.documents) {
                            val venuePopularityMap =
                                document[FireStoreConstants.venuePopularity] as HashMap<*, *>
                            val venuePopularity = VenuePopularity(
                                exclusively = venuePopularityMap[FireStoreConstants.exclusively] as Boolean,
                                popularity = venuePopularityMap[FireStoreConstants.popularity] as Boolean,
                                rating = venuePopularityMap[FireStoreConstants.rating] as Number
                            )
                            val venueInformationMap =
                                document[FireStoreConstants.venueInformation] as HashMap<*, *>
                            val venueInformation = VenueInformation(
                                address = venueInformationMap[FireStoreConstants.address] as String,
                                tel = venueInformationMap[FireStoreConstants.tel] as String,
                                isOpen = venueInformationMap[FireStoreConstants.isOpen] as Boolean,
                                url = venueInformationMap[FireStoreConstants.url] as String
                            )
                            val venueDeliveryMap =
                                document[FireStoreConstants.delivery] as HashMap<*, *>
                            val delivery = Delivery(
                                deliveryPrice = venueDeliveryMap[FireStoreConstants.deliveryPrice] as String,
                                deliveryTime = venueDeliveryMap[FireStoreConstants.deliveryTime] as String
                            )
                            val locationMap=document[FireStoreConstants.location] as HashMap<*,*>
                            val location=Location(
                                lat = locationMap[FireStoreConstants.lat] as Number,
                                lng = locationMap[FireStoreConstants.lng] as Number
                            )
                            val venue = Venue(
                                id = document.getString(FireStoreConstants.id) as String,
                                parentVenueId = document.getString(FireStoreConstants.parentVenueId) as String,
                                bribe = document.getBoolean(FireStoreConstants.bribe) as Boolean,
                                imageUrl = document.getString(FireStoreConstants.imageUrl) as String,
                                restaurant = document.getBoolean(FireStoreConstants.restaurant) as Boolean,
                                type = document.getString(FireStoreConstants.type) as String,
                                serviceFee = document.getDouble(FireStoreConstants.serviceFee) as Number,
                                venueName = document.getString(FireStoreConstants.venueName) as String,
                                venueText = document.getString(FireStoreConstants.venueText) as String,
                                venueLogoUrl = document.getString(FireStoreConstants.venueLogoUrl) as String,
                                venuePopularity = venuePopularity,
                                venueInformation = venueInformation,
                                delivery = delivery,
                                location = location
                            )
                            venue.id = document.id
                            venueList.add(venue)
                        }
                        continuation.resume(Resource.success(venueList))
                    }
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun getVenueCategory(): Resource<List<VenueCategory>> {
        return try {
            suspendCoroutine { continuation ->
                val venueCategoryList= mutableListOf<VenueCategory>()
                fireStore.collection(FireStoreCollectionConstants.venueCategory).get().addOnSuccessListener {querySnapshot->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else {
                        for (document in querySnapshot.documents) {
                            val venueCategory = VenueCategory(
                                id = document.getString(FireStoreConstants.id) as String,
                                title = document.getString(FireStoreConstants.title) as String,
                                imageUrl = document.getString(FireStoreConstants.imageUrl) as String,
                                restaurant = document.getBoolean(FireStoreConstants.restaurant) as Boolean
                            )
                            venueCategory.id = document.id
                            venueCategoryList.add(venueCategory)
                        }
                        continuation.resume(Resource.success(venueCategoryList))
                    }
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun getVenueDetailsItemVenue(): Resource<List<VenueDetailsItem>> {
        return try {
            suspendCoroutine { continuation ->
                val venueDetailsItemList= mutableListOf<VenueDetailsItem>()
                val venueDetailsList= mutableListOf<VenueDetails>()
                fireStore.collection(FireStoreCollectionConstants.venueDetailsItemRestaurant).get().addOnSuccessListener {querySnapshot->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else {
                        for (document in querySnapshot.documents) {
                            val arrayList =
                                document.get(FireStoreConstants.venueDetails) as? ArrayList<*>
                            arrayList?.let {
                                for (venueDetail in it) {
                                    if (venueDetail is HashMap<*, *>) {
                                        val venueDetails = VenueDetails(
                                            id = venueDetail[FireStoreConstants.id] as String,
                                            imageUrl = venueDetail[FireStoreConstants.imageUrl] as String,
                                            about = venueDetail[FireStoreConstants.about] as String,
                                            price = venueDetail[FireStoreConstants.price] as Number,
                                            popularity = venueDetail[FireStoreConstants.popularity] as Boolean,
                                            selected = venueDetail[FireStoreConstants.selected] as Boolean,
                                            count = venueDetail[FireStoreConstants.count] as Number,
                                            stock = venueDetail[FireStoreConstants.stock] as Number,
                                            menuName = venueDetail[FireStoreConstants.menuName] as String
                                        )
                                        venueDetailsList.add(venueDetails)
                                    }
                                }
                            }
                            val venueDetailsItem = VenueDetailsItem(
                                title = document.getString(FireStoreConstants.title) as String,
                                id = document.getString(FireStoreConstants.id) as String,
                                parentId = document.getString(FireStoreConstants.parentId) as String,
                                venueDetailList = venueDetailsList
                            )
                            venueDetailsItem.id = document.id
                            venueDetailsItemList.add(venueDetailsItem)
                        }
                        continuation.resume(Resource.success(venueDetailsItemList))
                    }
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun getVenueDetailsItemStore(): Resource<List<VenueDetailsItem>> {
        return try {
            suspendCoroutine { continuation ->
                val venueDetailsItemList= mutableListOf<VenueDetailsItem>()
                val storeMenuCategoryList= mutableListOf<StoreMenuCategory>()
                fireStore.collection(FireStoreCollectionConstants.venueDetailsItemStore).get().addOnSuccessListener {querySnapshot->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else {
                        for (document in querySnapshot.documents) {
                            val arrayList =
                                document.get(FireStoreConstants.storeMenuCategoryList) as? ArrayList<*>
                            arrayList?.let {
                                for (storeCategory in it) {
                                    if (storeCategory is HashMap<*, *>) {
                                        val storeMenuCategory = StoreMenuCategory(
                                            id = storeCategory[FireStoreConstants.id] as String,
                                            title = storeCategory[FireStoreConstants.title] as String,
                                            imageUrl = storeCategory[FireStoreConstants.imageUrl] as String,
                                        )
                                        storeMenuCategoryList.add(storeMenuCategory)
                                    }
                                }
                            }
                            val venueDetailsItem = VenueDetailsItem(
                                title = document.getString(FireStoreConstants.title) as String,
                                id = document.getString(FireStoreConstants.id) as String,
                                parentId = document.getString(FireStoreConstants.parentId) as String,
                                dummy = document.getBoolean(FireStoreConstants.dummy) as Boolean,
                                storeMenuCategory = storeMenuCategoryList
                            )
                            venueDetailsItem.id = document.id
                            venueDetailsItemList.add(venueDetailsItem)
                        }
                        continuation.resume(Resource.success(venueDetailsItemList))
                    }
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun getFavoriteVenue(userId:String): Resource<List<Venue>> {
        return try {
            suspendCoroutine { continuation ->
                val venueList= mutableListOf<Venue>()
                fireStore.collection(FireStoreCollectionConstants.favoriteVenue).document(userId)
                    .collection(FireStoreCollectionConstants.favVenues).get().addOnSuccessListener {querySnapshot->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else{
                    for (document in querySnapshot.documents){
                        val venuePopularityMap = document[FireStoreConstants.venuePopularity] as HashMap<*, *>
                        val venuePopularity=VenuePopularity(
                            exclusively = venuePopularityMap[FireStoreConstants.exclusively] as Boolean,
                            popularity = venuePopularityMap[FireStoreConstants.popularity] as Boolean,
                            rating = venuePopularityMap[FireStoreConstants.rating] as Number
                        )
                        val venueInformationMap = document[FireStoreConstants.venueInformation] as HashMap<*, *>
                        val venueInformation=VenueInformation(
                            address = venueInformationMap[FireStoreConstants.address] as String,
                            tel =venueInformationMap[FireStoreConstants.tel] as String,
                            isOpen = venueInformationMap[FireStoreConstants.isOpen] as Boolean,
                            url = venueInformationMap[FireStoreConstants.url] as String
                        )
                        val venueDeliveryMap = document[FireStoreConstants.delivery] as HashMap<*, *>
                        val delivery=Delivery(
                            deliveryPrice = venueDeliveryMap[FireStoreConstants.deliveryPrice] as String,
                            deliveryTime = venueDeliveryMap[FireStoreConstants.deliveryTime] as String
                        )
                        val locationMap=document[FireStoreConstants.location] as HashMap<*,*>
                        val location=Location(
                            lat = locationMap[FireStoreConstants.lat] as Number,
                            lng = locationMap[FireStoreConstants.lng] as Number
                        )
                        val venue=Venue(
                            id = document.getString(FireStoreConstants.id) as String,
                            parentVenueId = document.getString(FireStoreConstants.parentVenueId) as String,
                            bribe = document.getBoolean(FireStoreConstants.bribe) as Boolean,
                            imageUrl = document.getString(FireStoreConstants.imageUrl) as String,
                            restaurant = document.getBoolean(FireStoreConstants.restaurant) as Boolean,
                            type = document.getString(FireStoreConstants.type) as String,
                            serviceFee = document.getDouble(FireStoreConstants.serviceFee) as Number,
                            venueName = document.getString(FireStoreConstants.venueName) as String,
                            venueText = document.getString(FireStoreConstants.venueText) as String,
                            venueLogoUrl = document.getString(FireStoreConstants.venueLogoUrl) as String,
                            venuePopularity = venuePopularity,
                            venueInformation = venueInformation,
                            delivery = delivery,
                            location = location
                        )
                        venue.id=document.id
                        venueList.add(venue)
                    }
                    continuation.resume(Resource.success(venueList))
                    }
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun searchVenue(searchText: String): Resource<List<Venue>> {
        return try {
            suspendCoroutine { continuation ->
                val venueList= mutableListOf<Venue>()
                fireStore.collection(FireStoreCollectionConstants.venue).get().addOnSuccessListener {querySnapshot->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else {
                        for (document in querySnapshot.documents) {
                            val venuePopularityMap =
                                document[FireStoreConstants.venuePopularity] as HashMap<*, *>
                            val venuePopularity = VenuePopularity(
                                exclusively = venuePopularityMap[FireStoreConstants.exclusively] as Boolean,
                                popularity = venuePopularityMap[FireStoreConstants.popularity] as Boolean,
                                rating = venuePopularityMap[FireStoreConstants.rating] as Number
                            )
                            val venueInformationMap =
                                document[FireStoreConstants.venueInformation] as HashMap<*, *>
                            val venueInformation = VenueInformation(
                                address = venueInformationMap[FireStoreConstants.address] as String,
                                tel = venueInformationMap[FireStoreConstants.tel] as String,
                                isOpen = venueInformationMap[FireStoreConstants.isOpen] as Boolean,
                                url = venueInformationMap[FireStoreConstants.url] as String
                            )
                            val venueDeliveryMap =document[FireStoreConstants.delivery] as HashMap<*, *>
                            val delivery = Delivery(
                                deliveryPrice = venueDeliveryMap[FireStoreConstants.deliveryPrice] as String,
                                deliveryTime = venueDeliveryMap[FireStoreConstants.deliveryTime] as String
                            )
                            val locationMap=document[FireStoreConstants.location] as HashMap<*,*>
                            val location=Location(
                                lat = locationMap[FireStoreConstants.lat] as Number,
                                lng = locationMap[FireStoreConstants.lng] as Number
                            )
                            val venue = Venue(
                                id = document.getString(FireStoreConstants.id) as String,
                                parentVenueId = document.getString(FireStoreConstants.parentVenueId) as String,
                                bribe = document.getBoolean(FireStoreConstants.bribe) as Boolean,
                                imageUrl = document.getString(FireStoreConstants.imageUrl) as String,
                                restaurant = document.getBoolean(FireStoreConstants.restaurant) as Boolean,
                                type = document.getString(FireStoreConstants.type) as String,
                                serviceFee = document.getDouble(FireStoreConstants.serviceFee) as Number,
                                venueName = document.getString(FireStoreConstants.venueName) as String,
                                venueText = document.getString(FireStoreConstants.venueText) as String,
                                venueLogoUrl = document.getString(FireStoreConstants.venueLogoUrl) as String,
                                venuePopularity = venuePopularity,
                                venueInformation = venueInformation,
                                delivery = delivery,
                                location = location
                            )
                            if (venue.venueName.lowercase().contains(searchText.lowercase())){
                                venue.id = document.id
                                venueList.add(venue)
                            }
                        }
                        continuation.resume(Resource.success(venueList))
                    }
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun searchVenueDetailsItemStore(searchText: String): Resource<List<VenueDetailsItem>> {
        return try {
            suspendCoroutine { continuation ->
                val venueDetailsItemList= mutableListOf<VenueDetailsItem>()
                val storeMenuCategoryList= mutableListOf<StoreMenuCategory>()
                var filteredVenueDetailsItemList= listOf<VenueDetailsItem>()
                var filteredStoreMenuCategoryList: List<StoreMenuCategory>
                fireStore.collection(FireStoreCollectionConstants.venueDetailsItemStore).get().addOnSuccessListener {querySnapshot->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else {
                        for (document in querySnapshot.documents) {
                            val arrayList =
                                document.get(FireStoreConstants.storeMenuCategoryList) as? ArrayList<*>
                            arrayList?.let {
                                for (storeCategory in it) {
                                    if (storeCategory is HashMap<*, *>) {
                                        val storeMenuCategory = StoreMenuCategory(
                                            id = storeCategory[FireStoreConstants.id] as String,
                                            title = storeCategory[FireStoreConstants.title] as String,
                                            imageUrl = storeCategory[FireStoreConstants.imageUrl] as String,
                                        )
                                        storeMenuCategoryList.add(storeMenuCategory)
                                    }
                                }
                            }
                            val venueDetailsItem = VenueDetailsItem(
                                title = document.getString(FireStoreConstants.title) as String,
                                id = document.getString(FireStoreConstants.id) as String,
                                parentId = document.getString(FireStoreConstants.parentId) as String,
                                dummy = document.getBoolean(FireStoreConstants.dummy) as Boolean,
                                storeMenuCategory = storeMenuCategoryList
                            )
                            venueDetailsItemList.add(venueDetailsItem)
                        }

                        filteredStoreMenuCategoryList=storeMenuCategoryList.filter {
                            it.title.lowercase().contains(searchText.lowercase())
                        }
                        venueDetailsItemList.map {venueDetailsItem ->
                            venueDetailsItem.storeMenuCategory=filteredStoreMenuCategoryList
                            filteredStoreMenuCategoryList.map { storeMenuCategory ->
                                if (storeMenuCategory.title.lowercase().contains(searchText.lowercase()) ||
                                    venueDetailsItem.title.lowercase().contains(searchText.lowercase())){
                                    filteredVenueDetailsItemList=venueDetailsItemList
                                }
                            }
                        }
                        continuation.resume(Resource.success(filteredVenueDetailsItemList))
                    }
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun searchVenueDetailsItemVenue(searchText: String): Resource<List<VenueDetailsItem>> {
        return try {
            suspendCoroutine { continuation ->
                val venueDetailsItemList= mutableListOf<VenueDetailsItem>()
                val venueDetailsList= mutableListOf<VenueDetails>()
                var filteredVenueDetailsItemList= listOf<VenueDetailsItem>()
                var filteredVenueDetailsList: List<VenueDetails>
                fireStore.collection(FireStoreCollectionConstants.venueDetailsItemRestaurant).get().addOnSuccessListener {querySnapshot->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else {
                        for (document in querySnapshot.documents) {
                            val arrayList =
                                document.get(FireStoreConstants.venueDetails) as? ArrayList<*>
                            arrayList?.let {
                                for (venueDetail in it) {
                                    if (venueDetail is HashMap<*, *>) {
                                        val venueDetails = VenueDetails(
                                            id = venueDetail[FireStoreConstants.id] as String,
                                            imageUrl = venueDetail[FireStoreConstants.imageUrl] as String,
                                            about = venueDetail[FireStoreConstants.about] as String,
                                            price = venueDetail[FireStoreConstants.price] as Number,
                                            popularity = venueDetail[FireStoreConstants.popularity] as Boolean,
                                            selected = venueDetail[FireStoreConstants.selected] as Boolean,
                                            count = venueDetail[FireStoreConstants.count] as Number,
                                            stock = venueDetail[FireStoreConstants.stock] as Number,
                                            menuName = venueDetail[FireStoreConstants.menuName] as String
                                        )
                                            venueDetailsList.add(venueDetails)
                                    }
                                }
                            }
                                val venueDetailsItem = VenueDetailsItem(
                                    title = document.getString(FireStoreConstants.title) as String,
                                    id = document.getString(FireStoreConstants.id) as String,
                                    parentId = document.getString(FireStoreConstants.parentId) as String,
                                    venueDetailList = venueDetailsList
                                )
                                venueDetailsItemList.add(venueDetailsItem)
                        }
                        filteredVenueDetailsList=venueDetailsList.filter {
                            it.menuName.lowercase().contains(searchText.lowercase())
                        }
                        venueDetailsItemList.map {venueDetailsItem->
                            filteredVenueDetailsItemList= emptyList()
                            venueDetailsItem.venueDetailList=filteredVenueDetailsList
                            filteredVenueDetailsList.map {venueDetails->
                                if (venueDetails.menuName.lowercase().contains(searchText.lowercase()) ||
                                    venueDetailsItem.title.lowercase().contains(searchText.lowercase())){
                                    filteredVenueDetailsItemList=venueDetailsItemList
                                }
                            }
                        }
                        continuation.resume(Resource.success(filteredVenueDetailsItemList))
                    }
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun insertFavoriteVenue(venue: Venue,userId: String): Resource<CRUD> {
        return try {
            suspendCoroutine { continuation ->
                fireStore.collection(FireStoreCollectionConstants.favoriteVenue).document(userId)
                    .collection(FireStoreCollectionConstants.favVenues).document(venue.id).set(venue).addOnSuccessListener {
                        continuation.resume(Resource.success(CRUD(venue.id,1)))
                }.addOnFailureListener {e->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun deleteFavoriteVenue(documentId: String, userId: String): Resource<CRUD> {
        return try {
            suspendCoroutine { continuation ->
                fireStore.collection(FireStoreCollectionConstants.favoriteVenue).document(userId)
                    .collection(FireStoreCollectionConstants.favVenues).document(documentId).delete().addOnSuccessListener {
                        continuation.resume(Resource.success(CRUD(documentId,2)))
                  }.addOnFailureListener {e->
                     continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                    }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun updateVenueDetailsStock(
        documentId: String,
        venueDetailList: List<VenueDetails>
    ):Resource<CRUD> {
        return try {
            suspendCoroutine { continuation ->
                fireStore.collection(FireStoreCollectionConstants.venueDetailsItemRestaurant)
                    .document(documentId)
                    .update(FireStoreConstants.venueDetails, venueDetailList).addOnSuccessListener {
                        continuation.resume(Resource.success(CRUD(documentId, 3)))
                    }.addOnFailureListener { e ->
                        continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null))
                    }
            }
            } catch (e:Exception){
                Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null)
            }
    }

    override suspend fun insertVenueDetailsToRoom(venueDetailsRoom: VenueDetailsRoom) {
        dao.insertVenueDetail(venueDetailsRoom)
    }

    override suspend fun deleteVenueDetailsFromRoom(venueDetailsRoom: VenueDetailsRoom) {
        dao.deleteVenueDetail(venueDetailsRoom)
    }

    override suspend fun updateVenueDetailsFromRoom(venueDetailsRoom: VenueDetailsRoom) {
        dao.updateVenueDetail(venueDetailsRoom)
    }

    override suspend fun getVenueDetailsFromRoom(): List<VenueDetailsRoom> {
        return dao.getVenueDetails()
    }

    override suspend fun deleteAllVenueDetailsFromRoom() {
        dao.deleteAllVenueDetails()
    }

    override suspend fun getCountries(): Response<CountryDto> {
        return countryApi.getCountries()
    }

    override suspend fun getAddress(userId: String): Resource<List<Address>> {
        return try {
            suspendCoroutine { continuation ->
                val addressList= mutableListOf<Address>()
                fireStore.collection(FireStoreCollectionConstants.userAddress).document(userId)
                    .collection(FireStoreCollectionConstants.address).get().addOnSuccessListener {querySnapshot->
                        if (querySnapshot.isEmpty){
                            continuation.resume(Resource.success(emptyList()))
                        }else{
                            for (document in querySnapshot.documents){
                                val address=Address(
                                    id = document.getString(FireStoreConstants.id) as String,
                                    addressName = document.getString(FireStoreConstants.addressName) as String,
                                    buildingName = document.getString(FireStoreConstants.buildingName) as String,
                                    entrance = document.getLong(FireStoreConstants.entrance) as Number,
                                    floor = document.getLong(FireStoreConstants.floor) as Number,
                                    doorCode = document.getString(FireStoreConstants.doorCode) as String,
                                    apartment = document.getLong(FireStoreConstants.apartment) as Number,
                                    countryName = document.getString(FireStoreConstants.countryName) as String,
                                    selected = document.getBoolean(FireStoreConstants.selected) as Boolean
                                )
                                address.id=document.id
                                addressList.add(address)
                            }
                            continuation.resume(Resource.success(addressList))
                        }
                }.addOnFailureListener {e ->
                   continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null))
                    }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null)
        }
    }

    override suspend fun insertAddress(userId: String, address: Address): Resource<CRUD> {
        return try {
            suspendCoroutine { continuation ->
                fireStore.collection(FireStoreCollectionConstants.userAddress).document(userId).collection(FireStoreCollectionConstants.address)
                    .add(address).addOnSuccessListener {
                        continuation.resume(Resource.success(CRUD(it.id,1)))
                    }.addOnFailureListener {e ->
                        continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null))
                    }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null)
        }
    }

    override suspend fun deleteAddress(userId: String, documentId: String): Resource<CRUD> {
        return try {
            suspendCoroutine { continuation ->
                fireStore.collection(FireStoreCollectionConstants.userAddress).document(userId).collection(FireStoreCollectionConstants.address)
                    .document(documentId).delete().addOnSuccessListener {
                        continuation.resume(Resource.success(CRUD(documentId,2)))
                    }.addOnFailureListener {e ->
                        continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null))
                    }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null)
        }
    }

    override suspend fun updateAddress(
        userId: String,
        documentId: String,
        address: Address
    ): Resource<CRUD> {
        return try {
            suspendCoroutine { continuation ->
                fireStore.collection(FireStoreCollectionConstants.userAddress).document(userId).collection(FireStoreCollectionConstants.address)
                    .document(documentId).set(address).addOnSuccessListener {
                        continuation.resume(Resource.success(CRUD(documentId,3)))
                    }.addOnFailureListener {e ->
                        continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null))
                    }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null)
        }
    }

}