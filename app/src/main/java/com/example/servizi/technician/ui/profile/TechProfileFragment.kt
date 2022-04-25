package com.example.servizi.technician.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.distinctUntilChanged
import com.example.servizi.application.BaseFragment
import com.example.servizi.databinding.FragmentTechProfileBinding
import com.example.servizi.technician.model.TechRepository
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.network.TechApiService
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TechProfileFragment :
    BaseFragment<TechProfileViewModel, FragmentTechProfileBinding, TechRepository>() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.techProfileData.value == null) {
            viewModel.getTechProfile()
        }

        viewModel.techProfile.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    binding.loading.visible(false)
                    binding.techProfileContainer.visible(true)
                    viewModel.techProfileData.value = it.data.technician
                    binding.techProfileData = viewModel.techProfileData.value
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
}