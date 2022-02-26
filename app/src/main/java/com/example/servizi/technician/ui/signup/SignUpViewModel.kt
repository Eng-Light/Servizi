package com.example.servizi.technician.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servizi.technician.model.TechSignUpResponseData
import com.example.servizi.technician.model.TechnicianData
import com.example.servizi.technician.network.TechApi
import kotlinx.coroutines.launch

enum class TechSignUpApiStatus{LOADING, ERROR, DONE }

class SignUpViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _signUpLoadingStatus = MutableLiveData<TechSignUpApiStatus>()
    private val _signUpData = MutableLiveData<TechnicianData>()
    private val _signUpResponseData = MutableLiveData<TechSignUpResponseData>()

    // The external immutable LiveData for the request status
    val signUpLoadingStatus: LiveData<TechSignUpApiStatus> = _signUpLoadingStatus
    val signUpData: LiveData<TechnicianData> = _signUpData
    val signUpResponseData: LiveData<TechSignUpResponseData> = _signUpResponseData


    //Send SignUp Request
    private fun signUpApiRequest(tData:TechnicianData){
        viewModelScope.launch {
            _signUpLoadingStatus.value = TechSignUpApiStatus.LOADING
            try {
                val result = TechApi.techRetrofitService.techSignUpRequest(tData)
                _signUpResponseData.value = result
                Log.d("ViewModel_SignUpTech",_signUpResponseData.value.toString())
                _signUpLoadingStatus.value = TechSignUpApiStatus.DONE
            }catch (e:Exception){
                _signUpLoadingStatus.value = TechSignUpApiStatus.ERROR
                _signUpResponseData.value = TechSignUpResponseData("","")
            }
        }
    }
}