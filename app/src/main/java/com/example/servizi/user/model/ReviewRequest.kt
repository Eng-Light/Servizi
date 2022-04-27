package com.example.servizi.user.model

import com.squareup.moshi.Json

data class ReviewRequest(
    @Json(name = "content") val content: String,
    @Json(name = "rate") val rate: Int,
    @Json(name = "appointmentId") val appointmentId: String
)
