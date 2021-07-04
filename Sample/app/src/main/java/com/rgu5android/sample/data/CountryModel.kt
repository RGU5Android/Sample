package com.rgu5android.sample.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class CountryModel(
    @Json(name = "Code")
    val code: String,

    @Json(name = "ID")
    val id: Int,

    @Json(name = "Name")
    val name: String,

    @Json(name = "PhoneCode")
    val phoneCode: String?
) {
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/hampusborgos/country-flags/main/png250px/${
            code.lowercase(
                Locale.ENGLISH
            )
        }.png"
}