package com.example.servizi.technician.model

import com.example.servizi.application.BaseRepository
import com.example.servizi.technician.network.TechApiService

class TechRepository(
    private val dataSource: TechApiService
) : BaseRepository() {

    suspend fun getAppointments() = safeApiCall { dataSource.getAllAppointmentsAsync() }

    suspend fun changeAppointmentStatus(id: Int, status: String) = safeApiCall {
        dataSource.changeAppointmentStatusAsync(
            ChangeStatusRequest(id, status)
        )
    }

    suspend fun getTechProfile() = safeApiCall { dataSource.getTechProfileAsync() }
   suspend fun updateInfo(newdata: UpdateRequest) = safeApiCall { dataSource.updateInformation(newdata) }
}