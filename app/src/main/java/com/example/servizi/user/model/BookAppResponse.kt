package com.example.servizi.user.model

import com.squareup.moshi.Json

data class BookAppResponse(
    @Json(name = "msg") val msg: String,
    @Json(name = "appointmentId") val appointmentId: Number
)
