package com.example.servizi.technician.network

import com.example.servizi.technician.model.*
import com.example.servizi.technician.model.login.data.LoginData
import com.example.servizi.technician.model.login.data.LoginResponseData
import com.example.servizi.technician.model.signup.TechnicianData
import com.example.servizi.technician.model.signup.data.TechSignUpResponseData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

private const val BASE_URL = "https://servizi.seifahmed.com/auth/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface TechApiService {
    @POST("technician/signup")
    fun techSignUpRequestAsync(@Body techSignUpData: TechnicianData): Deferred<Response<TechSignUpResponseData>>

    @POST("technician/signin")
    suspend fun techSignInRequestAsync(@Body loginData: LoginData): LoginResponseData

    @GET("technician/allappointments")
    suspend fun getAllAppointmentsAsync(): com.example.servizi.technician.model.AppointmentsResponse

    @PUT("technician/changestatus/")
    suspend fun changeAppointmentStatusAsync(@Body request: ChangeStatusRequest): ChangeStatusResponse

    @GET("technician/profile")
    suspend fun getTechProfileAsync(): GetTechResponse

    @PUT("auth/technician/updateinfo")
    suspend fun updateInformation(@Body newdata: UpdateRequest) :UpdateResponse
}

object TechApi {
    val techRetrofitService: TechApiService by lazy { retrofit.create(TechApiService::class.java) }
}