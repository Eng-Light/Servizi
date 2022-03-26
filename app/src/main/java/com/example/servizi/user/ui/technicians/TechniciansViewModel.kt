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

    private val _location: MutableLiveData<NewLocation> = MutableLiveData()
    val location: LiveData<NewLocation> = _location

    private val _profession: MutableLiveData<String> = MutableLiveData()
    val profession: LiveData<String> = _profession

    private val _techs: MutableLiveData<Result<TechniciansResponse>> = MutableLiveData()
    val techs: LiveData<Result<TechniciansResponse>> = _techs

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

    fun setLocation(newLocation: NewLocation) {
        _location.value = newLocation
    }

    fun setProfession(profession: String) {
        _profession.value =
            profession.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}