package com.example.servizi.technician.model.login.data

import com.squareup.moshi.Json

data class LoginResponseData(
    @Json(name = "token") val techToken: String?,
    @Json(name = "expiresIn") val techExpiresIn: String?,
    @Json(name = "id") val techId: Int?
)