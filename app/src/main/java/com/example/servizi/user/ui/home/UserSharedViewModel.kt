package com.example.servizi.user.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserSharedViewModel : ViewModel() {
    val _techProf: MutableLiveData<String> = MutableLiveData()
    val techProf: LiveData<String> = _techProf
}