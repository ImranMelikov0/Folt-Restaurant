package com.imranmelikov.folt.domain.use_case

import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.data.remote.dto.CountryDto
import com.imranmelikov.folt.data.remote.dto.toCountry
import com.imranmelikov.folt.domain.model.Country
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.util.Resource
import javax.inject.Inject

class GetCountries @Inject constructor(private val repository: FoltRepository) {
    suspend fun getCountries():Resource<List<Country>>{
       return try {
           val response=repository.getCountries()
           if (response.isSuccessful){
               response.body()?.let {
                   return@let Resource.success(it.toCountry())
               }?:Resource.error(ErrorMsgConstants.errorFromFirebase, null)
           }else{
               Resource.error(ErrorMsgConstants.errorFromFirebase, null)
           }
        }catch (e:Exception){
           Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null)
        }
    }
}