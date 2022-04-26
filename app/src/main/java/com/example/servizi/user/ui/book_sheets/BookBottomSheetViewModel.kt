package com.example.servizi.user.ui.book_sheets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.servizi.application.BaseViewModel
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.BookAppRequestData
import com.example.servizi.user.model.BookAppResponse
import com.example.servizi.user.model.UserRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BookBottomSheetViewModel (private val repository: UserRepository) : BaseViewModel(repository) {

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    val _bookingResponse = MutableLiveData<Result<BookAppResponse>>()
    val bookingResponse: LiveData<Result<BookAppResponse>> = _bookingResponse

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