package com.example.servizi.technician.model.login.data

import com.squareup.moshi.Json

data class LoginData(
    @Json(name = "phone") val phone: String,
    @Json(name = "password") val password: String
)