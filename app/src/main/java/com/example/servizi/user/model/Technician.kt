package com.example.servizi.user.model

data class Technician(
    val firstName: String,
    val lastName: String,
    val email: String?,
    val phone: String,
    val governorate: String,
    val city: String,
    val profession: String
)