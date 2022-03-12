package com.example.servizi.user.ui.login_signup_pager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserPagerViewModel:ViewModel() {
    val pagerItem = MutableLiveData<Int>()
    //val pagerItem:LiveData<Int> = _pagerItem

    fun setPagerSelectedItem(item: Int) {
        pagerItem.value = item
    }
}