package com.example.servizi.user.network

import com.example.servizi.technician.model.login.data.LoginData
import com.example.servizi.technician.model.login.data.LoginResponseData
import com.example.servizi.user.model.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://servizi.seifahmed.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface UserApiService {

    @GET("/user/alltechnicians")
    suspend fun getTechniciansAsync(@Query(value = "profession") profession: String): TechsResponse

    @GET("/user/gettechnician")
    suspend fun getTechniciansReAsync(@Query(value = "id") id: Int): TechsReResponse

    @PUT("/user/cancelappointment")
    suspend fun cancelAppointment(@Query(value = "id") id: Int): CancelResponse

    @GET("/user/allappointments")
    suspend fun getAppointmentsAsync(): AppointmentsResponse

    @PUT("/auth/user/locupdate")
    suspend fun updateLoc(@Body newLoc: NewLocation): LocUpdateResponse

    @POST("/auth/user/signup")
    fun userSignUpRequestAsync(@Body userSignUpData: UserData): Deferred<Response<UserSignUpResponseData>>

    @POST("/user/createapp")
    suspend fun bookAppAsync(@Body bookAppRequestData: BookAppRequestData): BookAppResponse

    @POST("/auth/user/signin")
    suspend fun userSignInRequestAsync(@Body loginData: LoginData): LoginResponseData

    @POST("/user/postreview")
    suspend fun postReviewAsync(@Body reviewData: ReviewRequest): ReviewResponse

}

object UserApi {
    val UserRetrofitService: UserApiService by lazy { retrofit.create(UserApiService::class.java) }
}