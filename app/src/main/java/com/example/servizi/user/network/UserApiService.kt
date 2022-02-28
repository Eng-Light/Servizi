package com.example.servizi.user.network
import android.service.autofill.UserData
import com.example.servizi.user.model.UserSignUpResponseData
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

interface UserApiService {

    @POST("auth/user/signup")
    suspend fun userSignUpRequest(@Body userSignUpData: UserData): UserSignUpResponseData

    /**@POST("createtask")
    suspend fun createTask(@Body title: TaskTitle): CreatedTaskResponse

    @PUT("updatetask")
    suspend fun updateTask(@Body updatedTask: UpdatedTask): TaskMsg

    @DELETE("deletetask/")
    suspend fun deleteTask(@Query("id") id: Int): TaskMsg*/
}

object UserApi {
    val UserRetrofitService: UserApiService by lazy { retrofit.create(UserApiService::class.java) }
}