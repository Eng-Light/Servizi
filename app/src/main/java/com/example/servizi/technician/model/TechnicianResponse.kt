package com.example.servizi.technician.model

import com.squareup.moshi.Json

data class TechnicianResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "firstName") val firstName: String,
    @Json(name = "lastName") val lastName: String,
    @Json(name = "email") val email: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "city") val city: String,
    @Json(name = "governorate") val governorate: String,
    @Json(name = "profession") val profession: String,
    @Json(name = "createdAt") val createdAt: String
)