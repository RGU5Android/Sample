package com.rgu5android.sample.data

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    companion object {
        const val baseUrl: String = "https://connect.mindbodyonline.com"
    }

    @GET("/rest/worldregions/country")
    suspend fun getCountryList(): List<CountryModel>

    @GET("/rest/worldregions/country/{countryId}/province")
    suspend fun getProvinceForCountry(
        @Path("countryId") countryId: Int
    ): List<ProvinceModel>
}