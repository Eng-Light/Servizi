package com.example.servizi.technician.model.login

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: Int,
    val userExpiresIn: String,
    val techToken: String
)