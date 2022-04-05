package com.example.servizi.user.ui.my_orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.Appointment
import com.example.servizi.user.model.AppointmentsResponse
import com.example.servizi.user.model.CancelResponse
import com.example.servizi.user.model.UserRepository
import kotlinx.coroutines.launch

class MyOrdersViewModel(private val repository: UserRepository) : BaseViewModel(repository) {

    private val _ordersResponse = MutableLiveData<Result<AppointmentsResponse>>()
    val ordersResponse: LiveData<Result<AppointmentsResponse>> = _ordersResponse

    private val _ordersData = MutableLiveData<AppointmentsResponse>()
    val ordersData: LiveData<AppointmentsResponse> = _ordersData

    private val _cancelResponse = MutableLiveData<Result<CancelResponse>>()
    val cancelResponse: LiveData<Result<CancelResponse>> = _cancelResponse

    val inProgress = MutableLiveData<MutableList<Appointment>>()
    val accepted = MutableLiveData<MutableList<Appointment>>()
    val rejected = MutableLiveData<MutableList<Appointment>>()
    val canceled = MutableLiveData<MutableList<Appointment>>()
    val completed = MutableLiveData<MutableList<Appointment>>()

    val sortedOrders = MutableLiveData<MutableList<Appointment>>()

    fun gerOrders() {
        viewModelScope.launch {
            _ordersResponse.value = Result.Loading
            _ordersResponse.value = repository.getAppointments()
        }
    }

    fun cancelApp(id: Int) {
        viewModelScope.launch {
            _cancelResponse.value = Result.Loading
            _cancelResponse.value = repository.cancelApp(id)
        }
    }

    fun setOrdersData(orders: AppointmentsResponse) {
        _ordersData.value = orders
    }

    fun sortOrders() {
        val _inProgress: MutableList<Appointment> = mutableListOf()
        val _accepted: MutableList<Appointment> = mutableListOf()
        val _rejected: MutableList<Appointment> = mutableListOf()
        val _canceled: MutableList<Appointment> = mutableListOf()
        val _completed: MutableList<Appointment> = mutableListOf()
        for (appointment in ordersData.value!!.appointments) {
            when (appointment.status) {
                "inprogress" -> {
                    _inProgress.add(appointment)
                }
                "accepted" -> {
                    _accepted.add(appointment)
                }
                "rejected" -> {
                    _rejected.add(appointment)
                }
                "cancelled" -> {
                    _canceled.add(appointment)
                }
                "completed" -> {
                    _completed.add(appointment)
                }
            }
        }
        inProgress.value = _inProgress
        accepted.value = _accepted
        rejected.value = _rejected
        canceled.value = _canceled
        completed.value = _completed
    }

    fun setSortedOrders(position: Int) {
        when (position) {
            0 -> {
                sortedOrders.value = inProgress.value
            }
            1 -> {
                sortedOrders.value = accepted.value
            }
            2 -> {
                sortedOrders.value = rejected.value
            }
            3 -> {
                sortedOrders.value = canceled.value
            }
            4 -> {
                sortedOrders.value = completed.value
            }
        }
    }

}