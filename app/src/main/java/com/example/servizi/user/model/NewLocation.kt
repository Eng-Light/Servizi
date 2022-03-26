package com.example.servizi.user.model

import com.squareup.moshi.Json

data class NewLocation(
    @Json(name = "governorate") var governorate: String,
    @Json(name = "city") var city: String
)
