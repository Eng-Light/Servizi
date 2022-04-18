package com.example.servizi.technician.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.TechRepository
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.LocUpdateResponse
import com.example.servizi.user.model.NewLocation
import kotlinx.coroutines.launch

class TechSettingsViewModel(private val repository: TechRepository) : BaseViewModel(repository) {
    val _updateResponse: MutableLiveData<Result<LocUpdateResponse>> = MutableLiveData()
    val updateResponse: LiveData<Result<LocUpdateResponse>> = _updateResponse

    fun updateLoc(newLocation: NewLocation) = viewModelScope.launch {
        _updateResponse.value = Result.Loading
        //_updateResponse.value = repository.updateLoc(newLocation)
        //todo: add updateLoc
    }
}