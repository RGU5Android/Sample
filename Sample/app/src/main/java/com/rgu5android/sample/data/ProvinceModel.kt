package com.rgu5android.sample.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ProvinceModel(
    @Json(name = "Code")
    val code: String?,

    @Json(name = "CountryCode")
    val countryCode: String?,

    @Json(name = "ID")
    val id: Int?,

    @Json(name = "Name")
    val name: String?
)