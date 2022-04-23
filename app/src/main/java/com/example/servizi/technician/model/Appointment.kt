package com.example.servizi.technician.model

import android.os.Parcelable
import com.example.servizi.user.model.UserData
import kotlinx.parcelize.Parcelize

@Parcelize
data class Appointment(
    val id: Int,
    val date: String,
    val duration: String,
    val description: String,
    val address:String,
    val reviewed: Int,
    val status: String,
    val user:UserData
): Parcelable