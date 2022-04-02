package com.example.servizi.user.model

data class Appointments(
    val id: Int,
    val date: String,
    val duration: String,
    val description: String,
    val reviewed: Boolean,
    val status: String,
    val technician: Technician
)