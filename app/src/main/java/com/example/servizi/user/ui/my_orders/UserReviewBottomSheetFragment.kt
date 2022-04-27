package com.example.servizi.user.ui.my_orders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentUserReviewBottomSheetBinding
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.model.Appointment
import com.example.servizi.user.model.ReviewRequest
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.network.RemoteDataSource
import com.example.servizi.user.network.UserApiService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserReviewBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var userPreferences: UserPreferences
    private val remoteDataSource = RemoteDataSource()

    val binding: FragmentUserReviewBottomSheetBinding by lazy {
        FragmentUserReviewBottomSheetBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: UserReviewBottomSheetViewModel

    fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return UserRepository(api)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userPreferences = UserPreferences(requireContext())
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory)[UserReviewBottomSheetViewModel::class.java]
        lifecycleScope.launch { userPreferences.accessToken.first() }

        arguments?.get("app")?.let { it ->
            viewModel.ordersData.value = it as Appointment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonConfirm.setOnClickListener {
            review()
        }

        viewModel.reviewResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    binding.loading.visible(true)
                }
                is Result.Success -> {
                    binding.loading.visible(false)
                    Toast.makeText(
                        requireContext(),
                        "Review added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    dismiss()
                }
                is Result.Error -> {
                    binding.loading.visible(false)
                    Toast.makeText(
                        requireContext(),
                        "Error adding review ${it.errorCode}",
                        Toast.LENGTH_SHORT
                    ).show()
                    handleApiError(it) {
                        review()
                    }
                }
            }
        }
    }

    private fun review() {
        Log.d("Review", "Review: ${viewModel.ordersData.value?.id.toString()}")
        val reviewRequest = ReviewRequest(
            binding.description.text.toString().trim(),
            binding.clintRating.rating.toInt(),
            viewModel.ordersData.value?.id.toString()
        )
        if (validateReview(reviewRequest)) {
            viewModel.postReview(reviewRequest)
        }
    }

    private fun validateReview(review: ReviewRequest): Boolean {
        var valid = true

        binding.tiDescription1.error = null
        if (!stringValidator(review.content)) {
            binding.tiDescription1.error = "Comment must be more than 30 Char"
            valid = false
        }
        return valid
    }

    private fun stringValidator(_s: String): Boolean {
        return !(_s.isEmpty() || _s.length < 30)
    }
}