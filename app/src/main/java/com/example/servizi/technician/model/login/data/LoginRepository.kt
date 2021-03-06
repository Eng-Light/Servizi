package com.example.servizi.technician.model.login.data

import com.example.servizi.application.BaseRepository
import com.example.servizi.technician.model.login.LoggedInUser
import com.example.servizi.technician.network.TechApi
import com.example.servizi.user.network.UserApi

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: BaseRepository) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun loginTech(
        userPhone: String,
        password: String
    ) = dataSource.safeApiCall{
        TechApi.techRetrofitService.techSignInRequestAsync(LoginData(userPhone, password))
    }

    suspend fun loginUser(
        userPhone: String,
        password: String
    ) = dataSource.safeApiCall{
        UserApi.UserRetrofitService.userSignInRequestAsync(LoginData(userPhone, password))
    }


    /*private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }*/
}