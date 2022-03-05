package com.example.servizi.technician.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servizi.R
import com.example.servizi.technician.model.login.data.LoginRepository
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.TechLoginResponseData
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResponse = MutableLiveData<Result<TechLoginResponseData>>()
    val loginResponse: LiveData<Result<TechLoginResponseData>> = _loginResponse


    fun login(userPhone: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            _loginResponse.value = Result.Loading
            _loginResponse.value = loginRepository.loginTech(userPhone, password)
            Log.d("Test_SignIn_1", _loginResponse.value.toString())
        }
    }


    fun loginDataChanged(userPhone: String, password: String) {
        if (!isUserNameValid(userPhone)) {
            _loginForm.value = LoginFormState(userPhoneError = R.string.invalid_userPhone)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }


    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }


    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 7
    }

    /*fun tokenTimer(time:String?){

    }*/
}