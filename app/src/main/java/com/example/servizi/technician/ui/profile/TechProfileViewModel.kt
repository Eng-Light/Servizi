package com.example.servizi.technician.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.GetTechResponse
import com.example.servizi.technician.model.TechRepository
import com.example.servizi.technician.model.TechnicianResponse
import com.example.servizi.technician.model.login.data.Result
import kotlinx.coroutines.launch

class TechProfileViewModel(private val repository: TechRepository) : BaseViewModel(repository) {
    private val _techProfile: MutableLiveData<Result<GetTechResponse>> = MutableLiveData()
    val techProfile: LiveData<Result<GetTechResponse>> = _techProfile

    val techProfileData: MutableLiveData<GetTechResponse> = MutableLiveData()

    fun getTechProfile() {
        viewModelScope.launch {
            _techProfile.value = Result.Loading
            _techProfile.value = repository.getTechProfile()
        }
    }
}