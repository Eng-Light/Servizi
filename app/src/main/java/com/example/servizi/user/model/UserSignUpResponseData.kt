package com.example.servizi.user.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.lang.Exception

@JsonClass(generateAdapter = true)
data class UserSignUpResponseData (
    @Json(name = "msg") val msg: String,
    @Json(name = "userId") val userId: String,
        )
