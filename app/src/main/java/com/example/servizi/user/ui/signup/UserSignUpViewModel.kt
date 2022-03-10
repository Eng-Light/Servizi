package com.example.servizi.user.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servizi.user.model.SignUpResponseFail
import com.example.servizi.user.model.UserData
import com.example.servizi.user.model.UserSignUpResponseData
import com.example.servizi.user.model.UserSignUpResponseFail
import com.example.servizi.user.network.UserApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch


enum class UserSignUpApiStatus { LOADING, ERROR, DONE }

@Suppress("FunctionName")
class UserSignUpViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _userSignUpLoadingStatus = MutableLiveData<UserSignUpApiStatus>()
    private val _userSignUpData = MutableLiveData<UserData>()
    private val _userSignUpResponseData = MutableLiveData<UserSignUpResponseData>()
    private val _errorCode = MutableLiveData<Int?>()
    private val _errorMessage = MutableLiveData<UserSignUpResponseFail>()

    // The external immutable LiveData for the request status

    val userSignUpLoadingStatus: LiveData<UserSignUpApiStatus> = _userSignUpLoadingStatus
    val userSignUpData: LiveData<UserData> = _userSignUpData
    val userSignUpResponseData: LiveData<UserSignUpResponseData> = _userSignUpResponseData
    val errorCode: LiveData<Int?> = _errorCode
    val errorMessage: LiveData<UserSignUpResponseFail> = _errorMessage

    //Send SignUp Request
    private fun userSignUpApiRequest(uData: UserData) {
        viewModelScope.launch {
            _userSignUpLoadingStatus.value = UserSignUpApiStatus.LOADING
            try {
                val result = UserApi.UserRetrofitService.userSignUpRequestAsync(uData).await()
                _errorCode.value = result.code()
                if (result.isSuccessful) {
                    _userSignUpResponseData.value = result.body()
                    Log.d("Test_SignUp_5", _userSignUpResponseData.value.toString())
                    _userSignUpLoadingStatus.value = UserSignUpApiStatus.DONE
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<UserSignUpResponseFail>() {}.type
                    val errorResponse: UserSignUpResponseFail? =
                        gson.fromJson(result.errorBody()?.string(), type)
                    _errorMessage.value = errorResponse!!
                    _errorMessage.value?.let { Log.d("Test_SignUp_Error", it.toString()) }
                    _userSignUpLoadingStatus.value = UserSignUpApiStatus.ERROR
                }
                //Toast.makeText(coroutineContext,errorMessage.value,Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("Test_SignUp_4", e.toString())
                _errorMessage.value = UserSignUpResponseFail(
                    "Check Internet Connection", listOf(
                        SignUpResponseFail("", "", "", "")
                    )
                )
                _userSignUpLoadingStatus.value = UserSignUpApiStatus.ERROR
                _userSignUpResponseData.value = UserSignUpResponseData("", "")
            }
            resetStatus()
        }
    }

    fun setUserData(userData: UserData) {
        _userSignUpData.value = userData
    }


    fun signUpUser(Data: UserData) {
        userSignUpApiRequest(Data)
    }

    private fun resetStatus(){
        _userSignUpLoadingStatus.value?.declaringClass
    }
}
