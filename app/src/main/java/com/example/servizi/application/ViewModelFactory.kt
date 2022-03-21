package com.example.servizi.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.user.model.TechRepository
import com.example.servizi.user.ui.home.UserHomeViewModel
import com.example.servizi.user.ui.settings.SettingsViewModel
import com.example.servizi.user.ui.technicians.TechniciansViewModel

@Suppress("UNCHECKED_CAST")

class ViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserHomeViewModel::class.java) -> UserHomeViewModel() as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(repository as TechRepository) as T
            modelClass.isAssignableFrom(TechniciansViewModel::class.java) -> TechniciansViewModel(repository as TechRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}