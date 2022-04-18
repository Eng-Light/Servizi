package com.example.servizi.technician.model

import com.example.servizi.application.BaseRepository
import com.example.servizi.technician.network.TechApiService

class TechRepository(
    private val dataSource: TechApiService
) : BaseRepository() {

    suspend fun getAppointments() = safeApiCall { dataSource.getAllAppointmentsAsync() }

}