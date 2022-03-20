package com.example.servizi.technician.model.login.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
open class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                //handle loggedInUser authentication
                Result.Success(apiCall.invoke())
            } catch (e: Throwable) {
                when (e) {
                    is HttpException -> {
                        Result.Error(false, e.code(), e.response()?.errorBody())
                    }
                    else -> {
                        Result.Error(true, null, null)
                    }
                }
            }
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}