package com.imranmelikov.folt.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.imranmelikov.folt.constants.FireStoreConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.domain.model.Banner
import com.imranmelikov.folt.domain.model.Offer
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FoltRepositoryImpl(private val fireStore: FirebaseFirestore):FoltRepository {
override suspend fun getOffer(): Resource<List<Offer>> {
    return try {
        suspendCoroutine { continuation ->
            val offerList = mutableListOf<Offer>()
            fireStore.collection(FireStoreConstants.offer).get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val bannerMap = document[FireStoreConstants.banner] as HashMap<*, *>
                    val banner = Banner(
                        id = bannerMap[FireStoreConstants.id] as String,
                        imageUrl = bannerMap[FireStoreConstants.imageUrl] as String,
                        title = bannerMap[FireStoreConstants.title] as String,
                        text = bannerMap[FireStoreConstants.text] as String,
                        bribe = bannerMap[FireStoreConstants.bribe] as Boolean
                    )
                    banner.id=document.id
                    val parentVenue=document.getString(FireStoreConstants.parentVenue)?:""
                    val offer=Offer(banner,parentVenue)
                    offerList.add(offer)
                    println(offerList)
                }
                continuation.resume(Resource.success(offerList))
            }
                .addOnFailureListener { e ->
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.message}", null))
                }
        }
    } catch (e: Exception) {
        Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.message}", null)
    }
}


}