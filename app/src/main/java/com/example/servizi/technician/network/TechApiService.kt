package com.example.servizi.technician.network

import com.example.servizi.technician.model.TechSignUpResponseData
import com.example.servizi.technician.model.TechnicianData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = " https://servizi.seifahmed.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface TechApiService {

    @POST("auth/technician/signup")
    suspend fun techSignUpRequest(@Body techSignUpData: TechnicianData): TechSignUpResponseData

    /**@POST("createtask")
    suspend fun createTask(@Body title: TaskTitle): CreatedTaskResponse

    @PUT("updatetask")
    suspend fun updateTask(@Body updatedTask: UpdatedTask): TaskMsg

    @DELETE("deletetask/")
    suspend fun deleteTask(@Query("id") id: Int): TaskMsg*/
}

object TechApi {
    val techRetrofitService: TechApiService by lazy { retrofit.create(TechApiService::class.java) }
}