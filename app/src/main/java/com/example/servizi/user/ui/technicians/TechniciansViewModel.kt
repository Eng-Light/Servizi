package com.example.servizi.user.ui.technicians

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.TechRepository
import com.example.servizi.user.model.Technician
import com.example.servizi.user.model.TechniciansResponse
import kotlinx.coroutines.launch

class TechniciansViewModel(private val techRepository: TechRepository) : BaseViewModel(techRepository) {

    val _technicians: MutableLiveData<List<Technician>> = MutableLiveData()
    val technicians: LiveData<List<Technician>> = _technicians

    private val _techs: MutableLiveData<Result<TechniciansResponse>> = MutableLiveData()
    val techs: LiveData<Result<TechniciansResponse>> = _techs

    fun getTechs(profession: String) = viewModelScope.launch {
        _techs.value = Result.Loading
        _techs.value = techRepository.getTechs(profession)
    }
}