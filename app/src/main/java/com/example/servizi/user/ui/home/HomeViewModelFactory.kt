package com.example.servizi.user.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.user.model.TechRepository
import com.example.servizi.user.network.RemoteDataSource
import com.example.servizi.user.network.UserApiService

class HomeViewModelFactory(private val token: String? = null) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                techRepository = TechRepository(
                    dataSource = RemoteDataSource().buildApi(UserApiService::class.java, authToken = token)
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}