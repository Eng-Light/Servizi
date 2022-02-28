package com.example.servizi.technician.model

import com.squareup.moshi.Json

data class TechnicianData(

    @Json(name = "firstName") val firstName: String,
    @Json(name = "lastName") val lastName: String,
    @Json(name = "email") val email: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "city") val city: String,
    @Json(name = "governorate") val governorate: String,
    @Json(name = "natinalId") val natinalId: String,
    @Json(name = "profession") val profession: String,
    @Json(name = "age") val birthDate: String,
    @Json(name = "password") val password: String

    )


