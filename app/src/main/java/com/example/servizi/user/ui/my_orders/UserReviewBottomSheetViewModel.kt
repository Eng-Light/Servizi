package com.example.servizi.user.ui.my_orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.Appointment
import com.example.servizi.user.model.ReviewRequest
import com.example.servizi.user.model.ReviewResponse
import com.example.servizi.user.model.UserRepository
import kotlinx.coroutines.launch

class UserReviewBottomSheetViewModel(private val repository: UserRepository) :
    BaseViewModel(repository) {

    var ordersData = MutableLiveData<Appointment>()

    private val _reviewResponse = MutableLiveData<Result<ReviewResponse>>()
    val reviewResponse: LiveData<Result<ReviewResponse>> = _reviewResponse

    fun postReview(review: ReviewRequest) {
        viewModelScope.launch {
            _reviewResponse.value = Result.Loading
            _reviewResponse.value = repository.postReview(review)
        }
    }
}