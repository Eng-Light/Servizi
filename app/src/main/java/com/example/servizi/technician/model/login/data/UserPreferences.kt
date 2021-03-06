package com.example.servizi.technician.model.login.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.example.servizi.datastore.LoginTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

object TokenSerializer : Serializer<LoginTokens> {
    override val defaultValue: LoginTokens
        get() = LoginTokens.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LoginTokens {
        try {
            return LoginTokens.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: LoginTokens, output: OutputStream) {
        return t.writeTo(output)
    }
}

private val Context.loginDataStore: DataStore<LoginTokens> by dataStore(
    fileName = "LOGIN_DATA_STORE",
    serializer = TokenSerializer,
)

class UserPreferences(
    context: Context
) {

    private val appContext = context.applicationContext

    //get accessToken
    val accessToken: Flow<String?> = appContext.loginDataStore.data.map {
        it.token
    }

    //get expiration
    val expiration: Flow<String?> = appContext.loginDataStore.data.map {
        it.expiresIn
    }

    //get governorate
    val governorate: Flow<String?> = appContext.loginDataStore.data.map {
        it.governorate
    }

    //get city
    val city: Flow<String?> = appContext.loginDataStore.data.map {
        it.city
    }

    //get UserID
    val userId: Flow<String?> = appContext.loginDataStore.data.map {
        it.id
    }

    val usertype: Flow<String?> = appContext.loginDataStore.data.map {
        it.userType
    }

    suspend fun saveUserGovernorate(userGovernorate: String?) {
        appContext.loginDataStore.updateData {
            it.toBuilder()
                .setGovernorate(userGovernorate)
                .build()
        }
    }

    suspend fun saveUserCity(userCity: String?) {
        appContext.loginDataStore.updateData {
            it.toBuilder()
                .setCity(userCity)
                .build()
        }
    }

    suspend fun saveUserType(userType: String?) {
        appContext.loginDataStore.updateData {
            it.toBuilder()
                .setUserType(userType)
                .build()
        }
    }

    suspend fun saveUserId(userId: String?) {
        appContext.loginDataStore.updateData {
            it.toBuilder()
                .setId(userId)
                .build()
        }
    }

    suspend fun saveTokenExpire(expiration: String?) {
        appContext.loginDataStore.updateData {
            it.toBuilder()
                .setExpiresIn(expiration)
                .build()
        }
    }

    suspend fun saveAccessToken(
        accessToken: String?
    ) {
        appContext.loginDataStore.updateData {
            it.toBuilder()
                .setToken(accessToken)
                .build()
        }
    }

    suspend fun saveAccessData(
        accessToken: String?,
        expiration: String?,
        userType: String?,
        userId: String?
    ) {
        appContext.loginDataStore.updateData {
            it.toBuilder()
                .setToken(accessToken)
                .setExpiresIn(expiration)
                .setUserType(userType)
                .setId(userId)
                .build()
        }
    }

    suspend fun clear() {
        appContext.loginDataStore.updateData {
            it.toBuilder().clear().build()
        }
    }
}