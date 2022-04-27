package com.example.servizi.user.ui.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.TechReviewResponse
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ReviewsViewModel(private val repository: UserRepository) : BaseViewModel(repository) {
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _bookingResponse = MutableLiveData<Result<BookAppResponse>>()
    val bookingResponse: LiveData<Result<BookAppResponse>> = _bookingResponse

    private val _technicianResponse = MutableLiveData<Result<TechsReResponse>>()
    val technicianResponse: LiveData<Result<TechsReResponse>> = _technicianResponse

    val reviews:MutableLiveData<List<TechReviewResponse>> = MutableLiveData()
    val technician:MutableLiveData<TechsReResponse> = MutableLiveData()

    fun getTechnicianReviews(id: Int) {
        viewModelScope.launch {
            _technicianResponse.value = Result.Loading
            _technicianResponse.value = repository.getTechsRe(id)
        }
    }

    fun bookApp(bookAppRequestData: BookAppRequestData) {
        viewModelScope.launch {
            _bookingResponse.value = Result.Loading
            _bookingResponse.value = repository.bookApp(bookAppRequestData)
        }
    }

    fun setDate(month: Int, day: Int) {
        val date = Calendar.getInstance()
        date.set(date.get(Calendar.YEAR), month, day)
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        df.isLenient = false
        _date.value = df.format(date.time).toString()
    }
}