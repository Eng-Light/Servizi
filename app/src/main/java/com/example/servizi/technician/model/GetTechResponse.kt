package com.example.servizi.technician.model

import com.squareup.moshi.Json

data class GetTechResponse(
    @Json(name = "msg") val msg: String,
    @Json(name = "technician") val technician: TechnicianResponse,
    @Json(name = "reviews") val reviews: List<TechReviewResponse>
)
