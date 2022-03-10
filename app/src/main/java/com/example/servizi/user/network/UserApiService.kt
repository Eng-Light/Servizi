package com.example.servizi.user.network

import com.example.servizi.technician.model.login.data.LoginData
import com.example.servizi.technician.model.login.data.LoginResponseData
import com.example.servizi.user.model.UserData
import com.example.servizi.user.model.UserSignUpResponseData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://servizi.seifahmed.com/auth/user/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface UserApiService {
    @POST("signup")
    fun userSignUpRequestAsync(@Body userSignUpData: UserData): Deferred<Response<UserSignUpResponseData>>

    @POST("signin")
    suspend fun userSignInRequestAsync(@Body loginData: LoginData): LoginResponseData

}

object UserApi {
    val UserRetrofitService: UserApiService by lazy { retrofit.create(UserApiService::class.java) }
}