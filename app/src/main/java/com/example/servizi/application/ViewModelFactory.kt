package com.example.servizi.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.technician.model.TechRepository
import com.example.servizi.technician.ui.orders.TechOrdersBottomSheetViewModel
import com.example.servizi.technician.ui.orders.TechOrdersViewModel
import com.example.servizi.technician.ui.profile.TechProfileViewModel
import com.example.servizi.technician.ui.settings.TechSettingsViewModel
import com.example.servizi.technician.ui.update_info.UpdateTechProfileViewModel
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.ui.book_sheets.BookBottomSheetViewModel
import com.example.servizi.user.ui.booking.BookViewModel
import com.example.servizi.user.ui.home.UserSharedViewModel
import com.example.servizi.user.ui.my_orders.MyOrdersViewModel
import com.example.servizi.user.ui.my_orders.UserReviewBottomSheetViewModel
import com.example.servizi.user.ui.reviews.ReviewsViewModel
import com.example.servizi.user.ui.settings.UserSettingsViewModel
import com.example.servizi.user.ui.technicians.TechniciansViewModel

@Suppress("UNCHECKED_CAST")

class ViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserSharedViewModel::class.java) -> UserSharedViewModel() as T
            modelClass.isAssignableFrom(UserSettingsViewModel::class.java) -> UserSettingsViewModel(
                repository as UserRepository
            ) as T
            modelClass.isAssignableFrom(TechniciansViewModel::class.java) -> TechniciansViewModel(
                repository as UserRepository
            ) as T
            modelClass.isAssignableFrom(ReviewsViewModel::class.java) -> ReviewsViewModel(
                repository as UserRepository
            ) as T
            modelClass.isAssignableFrom(BookViewModel::class.java) -> BookViewModel(
                repository as UserRepository
            ) as T
            modelClass.isAssignableFrom(MyOrdersViewModel::class.java) -> MyOrdersViewModel(
                repository as UserRepository
            ) as T
            modelClass.isAssignableFrom(TechSettingsViewModel::class.java) -> TechSettingsViewModel(
                repository as TechRepository
            ) as T
            modelClass.isAssignableFrom(TechOrdersViewModel::class.java) -> TechOrdersViewModel(
                repository as TechRepository
            ) as T
            modelClass.isAssignableFrom(TechOrdersBottomSheetViewModel::class.java) -> TechOrdersBottomSheetViewModel(
                repository as TechRepository
            ) as T
            modelClass.isAssignableFrom(BookBottomSheetViewModel::class.java) -> BookBottomSheetViewModel(
                repository as UserRepository
            ) as T
            modelClass.isAssignableFrom(TechProfileViewModel::class.java) -> TechProfileViewModel(
                repository as TechRepository
            ) as T
            modelClass.isAssignableFrom(UserReviewBottomSheetViewModel::class.java) -> UserReviewBottomSheetViewModel(
                repository as UserRepository
            ) as T
            modelClass.isAssignableFrom(TechSettingsViewModel::class.java) -> TechSettingsViewModel(
                repository as TechRepository
            ) as T
            modelClass.isAssignableFrom(UpdateTechProfileViewModel::class.java) -> UpdateTechProfileViewModel(
                repository as TechRepository
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")

        }
    }
}