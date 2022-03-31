package com.example.servizi.user.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseRepository
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.LocUpdateResponse
import com.example.servizi.user.model.NewLocation
import com.example.servizi.user.model.UserRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: UserRepository) : BaseViewModel(repository) {


    val _updateResponse: MutableLiveData<Result<LocUpdateResponse>> = MutableLiveData()
    val updateResponse: LiveData<Result<LocUpdateResponse>> = _updateResponse

    fun updateLoc(newLocation: NewLocation) = viewModelScope.launch {
        _updateResponse.value = Result.Loading
        _updateResponse.value = repository.updateLoc(newLocation)
    }
}