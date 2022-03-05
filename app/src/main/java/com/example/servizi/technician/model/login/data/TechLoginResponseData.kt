package com.example.servizi.technician.model.login.data

import com.squareup.moshi.Json

data class TechLoginResponseData(
    @Json(name = "token") val techToken: String?,
    @Json(name = "expiresIn") val techExpiresIn: String?,
    @Json(name = "technicianId") val techId: Int?
)