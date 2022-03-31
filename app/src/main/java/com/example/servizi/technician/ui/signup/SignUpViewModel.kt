package com.example.servizi.technician.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servizi.technician.model.signup.data.TechSignUpResponseData
import com.example.servizi.technician.model.signup.data.TechSignUpResponseFail
import com.example.servizi.technician.model.signup.TechnicianData
import com.example.servizi.technician.model.signup.data.SignUpResponseFail
import com.example.servizi.technician.network.TechApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class TechSignUpApiStatus { LOADING, ERROR, DONE }

class SignUpViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _signUpLoadingStatus = MutableLiveData<TechSignUpApiStatus>()
    private val _signUpData = MutableLiveData<TechnicianData>()
    private val _signUpResponseData = MutableLiveData<TechSignUpResponseData>()
    private val _errorCode = MutableLiveData<Int?>()
    private val _errorMessage = MutableLiveData<TechSignUpResponseFail>()
    private val _birthDate = MutableLiveData<String>()

    //The external immutable LiveData for the request status
    val signUpLoadingStatus: LiveData<TechSignUpApiStatus> = _signUpLoadingStatus
    val signUpData: LiveData<TechnicianData> = _signUpData
    val signUpResponseData: LiveData<TechSignUpResponseData> = _signUpResponseData
    val errorCode: LiveData<Int?> = _errorCode
    val errorMessage: LiveData<TechSignUpResponseFail> = _errorMessage
    val birthDate: LiveData<String> = _birthDate


    //Send SignUp Request
    /**Don't forget to chick dependencies*/
    /**References ->
    https://tahaben.com.ly/2020/07/android-retrofit-handling-error-body-in-kotlin-coroutines/
    https://www.andreasjakl.com/how-to-retrofit-moshi-coroutines-recycler-view-for-rest-web-service-operations-with-kotlin-for-android/ */
    private fun signUpApiRequest(tData: TechnicianData) {
        viewModelScope.launch {
            _signUpLoadingStatus.value = TechSignUpApiStatus.LOADING
            try {
                val result = TechApi.techRetrofitService.techSignUpRequestAsync(tData).await()
                _errorCode.value = result.code()
                if (result.isSuccessful) {
                    _signUpResponseData.value = result.body()
                    Log.d("Test_SignUp_5", _signUpResponseData.value.toString())
                    _signUpLoadingStatus.value = TechSignUpApiStatus.DONE
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<TechSignUpResponseFail>() {}.type
                    val errorResponse: TechSignUpResponseFail? =
                        gson.fromJson(result.errorBody()?.string(), type)
                    _errorMessage.value = errorResponse!!
                    _errorMessage.value?.let { Log.d("Test_SignUp_Error", it.toString()) }
                    _signUpLoadingStatus.value = TechSignUpApiStatus.ERROR
                }
                //Toast.makeText(coroutineContext,errorMessage.value,Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("Test_SignUp_4", e.toString())
                _errorMessage.value = TechSignUpResponseFail(
                    "Check Internet Connection", listOf(
                        SignUpResponseFail("", "", "", "")
                    )
                )
                _signUpLoadingStatus.value = TechSignUpApiStatus.ERROR
                _signUpResponseData.value = TechSignUpResponseData("", "")
            }
            resetStatus()
        }
    }

    fun setTechData(techData: TechnicianData) {
        _signUpData.value = techData
    }

    fun setBirthDate(year: Int, month: Int, day: Int) {
        val date = Calendar.getInstance()
        date.set(year,month,day)
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        df.isLenient = false
        _birthDate.value = df.format(date.time).toString()
    }

    fun signUp(data: TechnicianData) {
        signUpApiRequest(data)
    }

    private fun resetStatus() {
        _signUpLoadingStatus.value?.declaringClass
    }
}