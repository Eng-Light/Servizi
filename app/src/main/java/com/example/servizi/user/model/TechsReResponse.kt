package com.example.servizi.user.model

import com.example.servizi.technician.model.TechReviewResponse
import com.squareup.moshi.Json

data class TechsReResponse(
    @Json(name = "firstName") val firstName: String,
    @Json(name = "lastName") val lastName: String,
    @Json(name = "email ") val email: String,
    @Json(name = "phone ") val phone: String,
    @Json(name = "governorate ") val governorate: String,
    @Json(name = "city ") val city: String,
    @Json(name = "profession ") val profession: String,
    @Json(name = "reviews ") var reviews: List<TechReviewResponse>,
    @Json(name = "rateAvg ") val rateAvg: Float
)
