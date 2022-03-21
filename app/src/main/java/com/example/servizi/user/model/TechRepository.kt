package com.example.servizi.user.model

import com.example.servizi.application.BaseRepository
import com.example.servizi.user.network.UserApiService

class TechRepository(
    private val dataSource: UserApiService
) : BaseRepository() {
    suspend fun getTechs(
        profession: String
    ) = safeApiCall { dataSource.getTechniciansAsync(profession) }
}