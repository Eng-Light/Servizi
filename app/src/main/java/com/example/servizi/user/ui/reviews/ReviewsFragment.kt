package com.example.servizi.user.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.servizi.R
import com.example.servizi.application.BaseFragment
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentUserReviewsBinding
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.model.BookAppRequestData
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.network.UserApiService
import com.example.servizi.user.ui.home.UserSharedViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ReviewsFragment :
    BaseFragment<ReviewsViewModel, FragmentUserReviewsBinding, UserRepository>() {
    private var newApp: BookAppRequestData? = null
    private lateinit var newcontrolar: NavController
    private val userSharedModel: UserSharedViewModel by activityViewModels()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())

        binding = getFragmentBinding(inflater, container)

        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory)[getViewModel()]

        lifecycleScope.launch { userPreferences.accessToken.first() }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rvShowReview.adapter = ReviewsAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshApp()

        binding.bttCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.bttBook.setOnClickListener {
            findNavController().navigate(R.id.action_reviewsFragment_to_bookBottomSheetFragment)
        }

        viewModel.technicianResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    binding.loading1.visible(true)
                }
                is Result.Success -> {
                    binding.loading1.visible(false)
                    viewModel.technician.value = it.data
                }
                is Result.Error -> {
                    binding.loading1.visible(false)
                    handleApiError(it) {
                        refreshApp()
                    }
                }
            }
        }

        viewModel.bookingResponse.observe(viewLifecycleOwner) {
            binding.loading1.visible(true)
            when (it) {
                is Result.Success -> {
                    binding.loading1.visible(false)
                    Toast.makeText(requireContext(), it.data.msg, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.navigation_my_orders)
                }
                is Result.Loading -> {
                    binding.loading1.visible(true)
                }
                is Result.Error -> {
                    handleApiError(it) {
                        viewModel.bookApp(newApp!!)
                    }
                    binding.loading1.visible(false)
                }
            }
        }
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            if (viewModel.technicianResponse.value == null) {
                viewModel.getTechnicianReviews(userSharedModel.techId.value!!)
            }
            binding.swipeToRefresh.isRefreshing = false
        }
        if (viewModel.technicianResponse.value == null) {
            viewModel.getTechnicianReviews(userSharedModel.techId.value!!)
        }
    }
}

