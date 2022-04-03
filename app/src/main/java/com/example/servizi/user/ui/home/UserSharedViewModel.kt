package com.example.servizi.user.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.servizi.user.model.NewLocation
import java.util.*

class UserSharedViewModel : ViewModel() {
    val _techProf: MutableLiveData<String> = MutableLiveData()
    val techProf: LiveData<String> = _techProf

    private val _location: MutableLiveData<NewLocation> = MutableLiveData()
    val location: LiveData<NewLocation> = _location

    private val _profession: MutableLiveData<String> = MutableLiveData()
    val profession: LiveData<String> = _profession

    private val _techId: MutableLiveData<Int> = MutableLiveData()
    val techId: LiveData<Int> = _techId

    fun setLocation(newLocation: NewLocation) {
        _location.value = newLocation
    }

    fun setProfession(profession: String) {
        _profession.value =
            profession.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    fun setTechId(id:Int){
        _techId.value = id
    }
}