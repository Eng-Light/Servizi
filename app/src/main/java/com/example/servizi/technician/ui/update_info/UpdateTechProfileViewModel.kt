package com.example.servizi.technician.ui.update_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.UpdateRequest
import com.example.servizi.technician.model.UpdateResponse
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.TechRepository
import kotlinx.coroutines.launch

class UpdateTechProfileViewModel (private val repository: TechRepository) : BaseViewModel(repository) {

   private val _updateinfoResponse: MutableLiveData<Result<UpdateResponse>> = MutableLiveData()
    val updateinfoResponse: LiveData<Result<UpdateResponse>> = _updateinfoResponse
    fun updateinfo(newinfo: UpdateRequest) = viewModelScope.launch {
        _updateinfoResponse.value = Result.Loading
        _updateinfoResponse.value = repository.updateInfo(newinfo)
    }
}