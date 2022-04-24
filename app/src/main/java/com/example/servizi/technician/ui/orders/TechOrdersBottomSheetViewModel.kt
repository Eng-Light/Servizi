package com.example.servizi.technician.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.Appointment
import com.example.servizi.technician.model.ChangeStatusResponse
import com.example.servizi.technician.model.TechRepository
import com.example.servizi.technician.model.login.data.Result
import kotlinx.coroutines.launch

class TechOrdersBottomSheetViewModel(private val repository: TechRepository) :
    BaseViewModel(repository) {

    var ordersData = MutableLiveData<Appointment>()

    private val _acceptResponse = MutableLiveData<Result<ChangeStatusResponse>>()
    val acceptResponse: LiveData<Result<ChangeStatusResponse>> = _acceptResponse

    private val _completeResponse = MutableLiveData<Result<ChangeStatusResponse>>()
    val completeResponse: LiveData<Result<ChangeStatusResponse>> = _completeResponse

    private val _rejectResponse = MutableLiveData<Result<ChangeStatusResponse>>()
    val rejectResponse: LiveData<Result<ChangeStatusResponse>> = _rejectResponse

    fun acceptOrder(id: Int, status: String) {
        viewModelScope.launch {
            _acceptResponse.value = Result.Loading
            _acceptResponse.value = repository.changeAppointmentStatus(id, status)
        }
    }

    fun rejectOrder(id: Int, status: String) {
        viewModelScope.launch {
            _rejectResponse.value = Result.Loading
            _rejectResponse.value = repository.changeAppointmentStatus(id, status)
        }
    }

    fun completeOrder(id: Int, status: String) {
        viewModelScope.launch {
            _completeResponse.value = Result.Loading
            _completeResponse.value = repository.changeAppointmentStatus(id, status)
        }
    }
}