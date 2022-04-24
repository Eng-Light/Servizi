package com.example.servizi.technician.model

import com.squareup.moshi.Json

data class ChangeStatusRequest(
    @Json(name = "id") val id: Int,
    @Json(name = "status") val status: String
)
