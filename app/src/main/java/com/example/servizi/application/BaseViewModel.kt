package com.example.servizi.application

import androidx.lifecycle.ViewModel

abstract class BaseViewModel(
    private val repository: BaseRepository
):ViewModel() {
}