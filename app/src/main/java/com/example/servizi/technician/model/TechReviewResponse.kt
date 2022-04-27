package com.example.servizi.technician.model

import com.squareup.moshi.Json

data class TechReviewResponse(
    @Json(name = "id") val id: String,
    @Json(name = "content") val content: String,
    @Json(name = "rate") val rate: Int,
    @Json(name = "technicianId") val technicianId: String,
    @Json(name = "userId") val userId: String,
    @Json(name = "appointmentId") val appointmentId: String,
    @Json(name = "createdAt") val createdAt: String,
)
