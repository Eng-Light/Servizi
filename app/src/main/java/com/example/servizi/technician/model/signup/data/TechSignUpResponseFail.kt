package com.example.servizi.technician.model.signup.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class TechSignUpResponseFail(
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
