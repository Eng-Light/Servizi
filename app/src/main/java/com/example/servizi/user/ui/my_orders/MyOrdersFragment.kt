package com.example.servizi.user.ui.my_orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.application.BaseFragment
import com.example.servizi.databinding.FragmentUserMyOrdersBinding
import com.example.servizi.databinding.FragmentUserReviewsBinding
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.network.UserApiService
import com.example.servizi.user.ui.reviews.ReviewsAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MyOrdersFragment :
    BaseFragment<MyOrdersViewModel, FragmentUserMyOrdersBinding, UserRepository>() {

    override fun getViewModel() = MyOrdersViewModel::class.java

    override fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return UserRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserMyOrdersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MyOrdersViewModel::class.java]
    }

}