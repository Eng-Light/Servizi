package com.example.servizi.user.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servizi.user.model.UserData
import com.example.servizi.user.model.UserSignUpResponseData
import com.example.servizi.user.model.UserSignUpResponseFail
import com.example.servizi.user.network.UserApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch


enum class UserSignUpApiStatus{LOADING, ERROR, DONE }

@Suppress("FunctionName")
class UserSignUpViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _usersignUpLoadingStatus = MutableLiveData<UserSignUpApiStatus>()
    private val _usersignUpData = MutableLiveData<UserData>()
    private val _usersignUpResponseData = MutableLiveData<UserSignUpResponseData>()
    private val _ErrorCode = MutableLiveData<Int?>()
    private val _ErrorMessage = MutableLiveData<UserSignUpResponseFail>()

    // The external immutable LiveData for the request status

    val usersignUpLoadingStatus: LiveData<UserSignUpApiStatus> = _usersignUpLoadingStatus
    val usersignUpData: LiveData<UserData> = _usersignUpData
    val usersignUpResponseData: LiveData<UserSignUpResponseData> = _usersignUpResponseData
    val ErrorCode: LiveData<Int?> = _ErrorCode
    val ErrorMessage: LiveData<UserSignUpResponseFail> = _ErrorMessage

    //Send SignUp Request
    private fun usersignUpApiRequest(uData:UserData){
        viewModelScope.launch {
            _usersignUpLoadingStatus.value = UserSignUpApiStatus.LOADING

            try {
                val result  = UserApi.UserRetrofitService. userSignUpRequestAsync(uData).await()
                _ErrorCode.value = result.code()
                if (result.isSuccessful) {
                    _usersignUpResponseData.value = result.body()
                    Log.d("Test_SignUp_5", _usersignUpResponseData.value.toString())
                    _usersignUpLoadingStatus.value = UserSignUpApiStatus.DONE
                } else {
                    val Gsonn = Gson()
                    val Type = object : TypeToken<UserSignUpResponseFail>() {}.type
                    val ErrorResponse: UserSignUpResponseFail? = Gsonn.fromJson(result.errorBody()?.string(), Type)

                    _ErrorMessage.value = ErrorResponse!!
                    _ErrorMessage.value?.let { Log.d("Test_SignUp_Error", it.toString()) }
                    _usersignUpLoadingStatus.value = UserSignUpApiStatus.ERROR
                }
                //Toast.makeText(coroutineContext,errorMessage.value,Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("Test_SignUp_4", e.toString())
                _usersignUpLoadingStatus.value = UserSignUpApiStatus.ERROR
                _usersignUpResponseData.value = UserSignUpResponseData("", "")
            }
        }
    }
    fun setUserData(userData: com.example.servizi.user.model.UserData) {
        _usersignUpData.value = userData
    }


    fun signUpUser(Data: UserData) {
        usersignUpApiRequest(Data)
    }
    fun ResetStatus(){
        _usersignUpLoadingStatus.value = UserSignUpApiStatus.LOADING
    }
}
