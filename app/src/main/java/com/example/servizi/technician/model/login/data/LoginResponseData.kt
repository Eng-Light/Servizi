package com.example.servizi.technician.model.login.data

import com.squareup.moshi.Json

data class LoginResponseData(
    @Json(name = "token") val userToken: String?,
    @Json(name = "expiresIn") val userExpiresIn: String?,
    @Json(name = "id") val userId: Int?,
    @Json(name = "governorate") val governorate: String? = "",
    @Json(name = "city") val city: String? = "",
)