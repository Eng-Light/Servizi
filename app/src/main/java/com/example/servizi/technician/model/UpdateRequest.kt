package com.example.servizi.technician.model

import com.squareup.moshi.Json

class UpdateRequest (
    @Json(name = "firstName") val firstName: String,
    @Json(name = "lastName") val lastName: String,
    @Json(name = "city") val city: String,
    @Json(name = "governorate") val governorate: String

)