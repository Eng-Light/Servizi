package com.example.servizi.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.technician.model.login.data.BaseRepository
import com.example.servizi.user.model.TechRepository
import com.example.servizi.user.ui.home.HomeViewModel

@Suppress("UNCHECKED_CAST")

class ViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as TechRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}