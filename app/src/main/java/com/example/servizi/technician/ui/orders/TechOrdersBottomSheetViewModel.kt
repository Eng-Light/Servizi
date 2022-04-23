package com.example.servizi.technician.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.servizi.technician.model.Appointment

class TechOrdersBottomSheetViewModel : ViewModel() {

    var ordersData = MutableLiveData<Appointment>()

}