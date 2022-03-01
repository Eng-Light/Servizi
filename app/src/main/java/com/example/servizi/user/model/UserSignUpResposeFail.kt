package com.example.servizi.user.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UserSignUpResponseFail(
    @Json(name = "msg") val msg:String,
    @Json(name = "data") val data:List<SignUpResponseFail>
)


@JsonClass(generateAdapter = true)
data class SignUpResponseFail(
    @Json(name = "value") val value: String  = "",
    @Json(name = "msg") val msg: String = "",
    @Json(name = "param") val param: String = "",
    @Json(name = "location") val location: String = "",
)
