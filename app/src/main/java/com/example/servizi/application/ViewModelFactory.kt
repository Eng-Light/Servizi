package com.example.servizi.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.ui.booking.BookViewModel
import com.example.servizi.user.ui.home.UserSharedViewModel
import com.example.servizi.user.ui.my_orders.MyOrdersViewModel
import com.example.servizi.user.ui.reviews.ReviewsViewModel
import com.example.servizi.user.ui.settings.SettingsViewModel
import com.example.servizi.user.ui.technicians.TechniciansViewModel

@Suppress("UNCHECKED_CAST")

class ViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserSharedViewModel::class.java) -> UserSharedViewModel() as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(
                repository as UserRepository
            ) as T
            modelClass.isAssignableFrom(TechniciansViewModel::class.java) -> TechniciansViewModel(
                repository as UserRepository
            ) as T
            modelClass.isAssignableFrom(ReviewsViewModel::class.java) -> ReviewsViewModel() as T
            modelClass.isAssignableFrom(BookViewModel::class.java) -> BookViewModel(
                repository as UserRepository
            ) as T
            modelClass.isAssignableFrom(MyOrdersViewModel::class.java) -> MyOrdersViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}