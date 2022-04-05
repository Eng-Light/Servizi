package com.example.servizi.user.model

data class Appointment(
    val id: Int,
    val date: String,
    val duration: String,
    val description: String,
    val address:String,
    val reviewed: Int,
    val status: String,
    val technician: Technician
)