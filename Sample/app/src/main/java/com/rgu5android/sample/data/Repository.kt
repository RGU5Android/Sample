package com.rgu5android.sample.data

import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getCountryList() = apiService.getCountryList()

    suspend fun getProvinceForCountry(countryId: Int) = apiService.getProvinceForCountry(countryId)
}