package com.example.servizi.technician.model.login.data

import okhttp3.ResponseBody

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Result<Nothing>()
    object Loading:Result<Nothing>()

    //Print Result
    override fun toString(): String {
        return when (this) {
            is Success -> "$data"
            is Error -> "Error[exception=$errorBody]"
            Loading -> "Loading"
        }
    }
}