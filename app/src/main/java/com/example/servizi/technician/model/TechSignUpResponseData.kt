package com.example.servizi.technician.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.lang.Exception

@JsonClass(generateAdapter = true)
data class TechSignUpResponseData(
    @Json(name = "msg") val msg: String,
    @Json(name = "technicianId") val technicianId: String,
)
