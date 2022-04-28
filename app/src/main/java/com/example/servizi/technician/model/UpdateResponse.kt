package com.example.servizi.technician.model

import com.squareup.moshi.Json

data class UpdateResponse (
    @Json(name = "msg") val msg: String
        )
