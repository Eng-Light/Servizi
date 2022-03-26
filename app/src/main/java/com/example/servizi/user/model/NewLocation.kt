package com.example.servizi.user.model

import com.squareup.moshi.Json

data class NewLocation(
    @Json(name = "governorate") val governorate: String,
    @Json(name = "city") val city: String
)
