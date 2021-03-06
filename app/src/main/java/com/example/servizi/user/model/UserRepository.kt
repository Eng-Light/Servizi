package com.example.servizi.user.model

import com.example.servizi.application.BaseRepository
import com.example.servizi.user.network.UserApiService

class UserRepository(
    private val dataSource: UserApiService
) : BaseRepository() {

    suspend fun getTechs(
        profession: String
    ) = safeApiCall { dataSource.getTechniciansAsync(profession) }

    suspend fun updateLoc(
        newLocation: NewLocation
    ) = safeApiCall { dataSource.updateLoc(newLocation) }

    suspend fun bookApp(
        bookAppRequestData: BookAppRequestData
    ) = safeApiCall { dataSource.bookAppAsync(bookAppRequestData) }

    suspend fun getTechsRe(
        id: Int
    ) = safeApiCall { dataSource.getTechniciansReAsync(id) }

    suspend fun cancelApp(
        id: Int
    ) = safeApiCall { dataSource.cancelAppointment(id) }

    suspend fun getAppointments() = safeApiCall { dataSource.getAppointmentsAsync() }

    suspend fun postReview(
        review: ReviewRequest
    ) = safeApiCall { dataSource.postReviewAsync(review) }
}