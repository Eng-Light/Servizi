package com.example.servizi.technician.model

import com.squareup.moshi.Json

data class TechSignUpResponseData(
    @Json(name = "msg") val msg: String,
    @Json(name = "technicianId") val technicianId: String,
)
