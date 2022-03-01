package com.example.servizi.user.network
import com.example.servizi.user.model.UserData
import com.example.servizi.user.model.UserSignUpResponseData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
private const val BASE_URL = " https://servizi.seifahmed.com/auth/user/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface UserApiService {

    @POST("auth/user/signup")
     fun userSignUpRequestAsync(@Body userSignUpData: UserData): Deferred<Response<UserSignUpResponseData>>


}

object UserApi {
    val UserRetrofitService: UserApiService by lazy { retrofit.create(UserApiService::class.java) }
}