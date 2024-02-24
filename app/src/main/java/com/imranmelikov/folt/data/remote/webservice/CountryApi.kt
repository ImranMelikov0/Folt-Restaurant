package com.imranmelikov.folt.data.remote.webservice

import com.imranmelikov.folt.constants.APIConstants
import com.imranmelikov.folt.data.remote.dto.CountryDto
import retrofit2.Response
import retrofit2.http.GET

interface CountryApi {
    @GET(APIConstants.countryQuery)
    suspend fun getCountries():Response<CountryDto>
}