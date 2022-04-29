package com.example.servizi.user.model

import com.squareup.moshi.Json

data class User(
    @Json(name = "firstName") val firstName: String,
    @Json(name = "lastName") val lastName: String,
)
