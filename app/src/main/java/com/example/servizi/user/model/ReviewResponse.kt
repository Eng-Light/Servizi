package com.example.servizi.user.model

import com.squareup.moshi.Json

data class ReviewResponse(
    @Json(name = "msg") val msg: String,
)