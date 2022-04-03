package com.example.servizi.user.ui.technicians

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.*
import kotlinx.coroutines.launch
import java.util.*

class TechniciansViewModel(private val userRepository: UserRepository) :
    BaseViewModel(userRepository) {

    val _technicians: MutableLiveData<List<Technician>> = MutableLiveData()
    val technicians: LiveData<List<Technician>> = _technicians

    private val _techs: MutableLiveData<Result<TechsResponse>> = MutableLiveData()
    val techs: LiveData<Result<TechsResponse>> = _techs

    private val _updateResponse: MutableLiveData<Result<LocUpdateResponse>> = MutableLiveData()
    val updateResponse: LiveData<Result<LocUpdateResponse>> = _updateResponse

    fun getTechs(profession: String) = viewModelScope.launch {
        _techs.value = Result.Loading
        _techs.value = userRepository.getTechs(profession)
    }

    fun updateLoc(newLocation: NewLocation) = viewModelScope.launch {
        _updateResponse.value = Result.Loading
        _updateResponse.value = userRepository.updateLoc(newLocation)
    }
}