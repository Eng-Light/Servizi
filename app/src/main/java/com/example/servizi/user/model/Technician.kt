package com.example.servizi.user.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Technician(
    val id: Int?,
    val firstName: String,
    val lastName: String,
    val email: String?,
    val phone: String,
    val governorate: String,
    val city: String,
    val profession: String,
    val rateAvg: Float,
) : Parcelable