package com.imranmelikov.folt.domain.repository

import com.imranmelikov.folt.domain.model.Offer
import com.imranmelikov.folt.util.Resource

interface FoltRepository {
    suspend fun getOffer():Resource<List<Offer>>
}