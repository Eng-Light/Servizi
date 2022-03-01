package com.example.servizi.technician.network

import com.example.servizi.technician.model.signup.data.TechSignUpResponseData
import com.example.servizi.technician.model.signup.TechnicianData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://servizi.seifahmed.com/auth/technician/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface TechApiService {
    @POST("signup")
    fun techSignUpRequestAsync(@Body techSignUpData: TechnicianData): Deferred<Response<TechSignUpResponseData>>
}

object TechApi {
    val techRetrofitService: TechApiService by lazy { retrofit.create(TechApiService::class.java) }
}