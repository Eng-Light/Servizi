package com.example.servizi.user.model

import com.squareup.moshi.Json

data class LocUpdateResponse(
    @Json(name = "msg") val msg: String
)
