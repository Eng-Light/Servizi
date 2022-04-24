package com.example.servizi.technician.model

import com.squareup.moshi.Json

data class ChangeStatusResponse(
    @Json(name = "msg") val msg: String,
)
