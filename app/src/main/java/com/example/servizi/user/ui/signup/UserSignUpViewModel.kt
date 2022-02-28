package com.example.servizi.user.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.service.autofill.UserData
import com.example.servizi.user.model.UserSignUpResponseData
import com.example.servizi.user.network.UserApi
import kotlinx.coroutines.launch


enum class UserSignUpApiStatus{LOADING, ERROR, DONE }

class UserSignUpViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _signUpLoadingStatus = MutableLiveData<UserSignUpApiStatus>()
    private val _signUpData = MutableLiveData<UserData>()
    private val _signUpResponseData = MutableLiveData<UserSignUpResponseData>()

    // The external immutable LiveData for the request status
    val signUpLoadingStatus: LiveData<UserSignUpApiStatus> = _signUpLoadingStatus
    val signUpData: LiveData<UserData> = _signUpData
    val signUpResponseData: LiveData<UserSignUpResponseData> = _signUpResponseData


    //Send SignUp Request
    private fun signUpApiRequest(uData:UserData){
        viewModelScope.launch {
            _signUpLoadingStatus.value = UserSignUpApiStatus.LOADING
            try {
                val result = UserApi.UserRetrofitService.userSignUpRequest(uData)
                _signUpResponseData.value = result
                Log.d("ViewModel_SignUpTech",_signUpResponseData.value.toString())
                _signUpLoadingStatus.value = UserSignUpApiStatus.DONE
            }catch (e:Exception){
                _signUpLoadingStatus.value = UserSignUpApiStatus.ERROR
                _signUpResponseData.value = UserSignUpResponseData("","")
            }
        }
    }
}