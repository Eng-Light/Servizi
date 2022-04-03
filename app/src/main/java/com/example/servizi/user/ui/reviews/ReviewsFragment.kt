package com.example.servizi.user.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.application.BaseFragment
import com.example.servizi.databinding.FragmentUserReviewsBinding
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.network.UserApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ReviewsFragment : BaseFragment<ReviewsViewModel, FragmentUserReviewsBinding, UserRepository>() {

    override fun getViewModel() = ReviewsViewModel::class.java

    override fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return UserRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserReviewsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ReviewsAdapter()
        adapter.submitList(
            arrayListOf(
                "Nour", "Mayar", "Manar", "Saif", "Nada", "Talaa", "Khaled", "Abd Allah",
                "Jasmin",
                "Talaa",
                "Khaled",
                "Abd Allah",
                "Jasmin"
            )
        )
        binding.rvShowReview.adapter = adapter
    }

}