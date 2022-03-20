package com.example.servizi.user.ui.technicians

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.servizi.user.model.Technician

class TechniciansViewModel : ViewModel() {
    val _technicians: MutableLiveData<List<Technician>> = MutableLiveData()
    val technicians: LiveData<List<Technician>> = _technicians
}