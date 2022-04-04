package com.example.servizi.user.model

import com.squareup.moshi.Json

data class BookAppRequestData(
    @Json(name = "date") val date: String,
    @Json(name = "address") val address: String,
    @Json(name = "duration") val duration: String,
    @Json(name = "description") val description: String,
    @Json(name = "technicianId") val technicianId: Int
)
