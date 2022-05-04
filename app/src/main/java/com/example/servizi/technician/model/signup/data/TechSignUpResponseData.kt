package com.example.servizi.technician.model.signup.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TechSignUpResponseData(
    @Json(name = "msg") val msg: String,
    @Json(name = "technicianId") val technicianId: String,
)
