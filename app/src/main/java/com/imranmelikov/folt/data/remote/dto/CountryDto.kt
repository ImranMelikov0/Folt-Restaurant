package com.imranmelikov.folt.data.remote.dto

import com.imranmelikov.folt.domain.model.Country

data class CountryDto(
    val `data`: List<Data>,
    val error: Boolean,
    val msg: String
)
fun CountryDto.toCountry(): List<Country> {
    return data.map { Country(it.name,it.capital) }
}