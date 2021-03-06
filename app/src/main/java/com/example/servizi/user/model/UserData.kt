package com.example.servizi.user.model


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(

    @Json(name = "firstName") val firstName: String,
    @Json(name = "lastName") val lastName: String,
    @Json(name = "email") val email: String?,
    @Json(name = "phone") val phone: String,
    @Json(name = "city") val city: String,
    @Json(name = "governorate") val governorate: String,
    @Json(name = "password") val password: String?,

    ): Parcelable
