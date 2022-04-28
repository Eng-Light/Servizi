package com.example.servizi.technician.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import com.example.servizi.application.BaseFragment
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentTechProfileBinding
import com.example.servizi.technician.model.TechRepository
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.network.TechApiService
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.ui.reviews.ReviewsAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TechProfileFragment : BaseFragment<TechProfileViewModel, FragmentTechProfileBinding, TechRepository>() {
    override fun getViewModel() = TechProfileViewModel::class.java

    override fun getFragmentRepository(): TechRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(TechApiService::class.java, token)
        return TechRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTechProfileBinding.inflate(inflater, container, false)

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
        binding.rvShowReview.adapter = ReviewsAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshApp()

        viewModel.techProfile.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    binding.loading.visible(false)
                    binding.techProfileContainer.visible(true)
                    binding.techProfile = it.data
                }
                is Result.Loading -> {
                    binding.loading.visible(true)
                    binding.techProfileContainer.visible(false)
                    viewModel.techProfileData.value = null
                }
                is Result.Error -> {
                    binding.loading.visible(false)
                    binding.techProfileContainer.visible(true)
                    handleApiError(it) {
                        viewModel.getTechProfile()
                    }
                }
            }
        }
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            if (viewModel.techProfileData.value == null) {
                viewModel.getTechProfile()
            }
            binding.swipeToRefresh.isRefreshing = false
        }
        if (viewModel.techProfileData.value == null) {
            viewModel.getTechProfile()
        }
    }
}