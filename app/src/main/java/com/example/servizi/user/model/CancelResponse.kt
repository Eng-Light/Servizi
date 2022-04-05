package com.example.servizi.user.model

import com.squareup.moshi.Json

data class CancelResponse(
    @Json(name = "msg") val msg:String
)
