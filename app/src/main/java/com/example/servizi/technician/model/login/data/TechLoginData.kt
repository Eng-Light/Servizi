package com.example.servizi.technician.model.login.data

import com.squareup.moshi.Json

data class TechLoginData(
    @Json(name = "phone") val techPhone: String,
    @Json(name = "password") val techPassword: String
)