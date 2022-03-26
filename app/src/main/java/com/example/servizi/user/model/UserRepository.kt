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
}