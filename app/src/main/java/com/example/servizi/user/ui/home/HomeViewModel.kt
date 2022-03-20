package com.example.servizi.user.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.TechRepository
import com.example.servizi.user.model.TechniciansResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val techRepository: TechRepository) : ViewModel() {

    private val _techs: MutableLiveData<Result<TechniciansResponse>> = MutableLiveData()
    val user: LiveData<Result<TechniciansResponse>> = _techs

    fun getTechs(profession:String)=viewModelScope.launch {
        _techs.value = Result.Loading
        _techs.value = techRepository.getTechs(profession)
    }
}