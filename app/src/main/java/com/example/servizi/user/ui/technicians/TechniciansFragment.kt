package com.example.servizi.user.ui.technicians

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.servizi.application.BaseFragment
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentTechniciansBinding
import com.example.servizi.databinding.TechHomeFragmentBinding
import com.example.servizi.databinding.UserHomeFragmentBinding
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.model.TechRepository
import com.example.servizi.user.model.Technician
import com.example.servizi.user.network.UserApiService
import com.example.servizi.user.ui.home.UserHomeViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TechniciansFragment :
    BaseFragment<TechniciansViewModel, FragmentTechniciansBinding, TechRepository>() {

    private val viewModel1: UserHomeViewModel by activityViewModels()

    override fun getViewModel() = TechniciansViewModel::class.java

    override fun getFragmentRepository(): TechRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return TechRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTechniciansBinding.inflate(inflater, container, false)

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
        binding.rvShowData.adapter = TechsAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingProgressBar = binding.loading
        val tvError = binding.tvError

        viewModel1.techProf.observe(viewLifecycleOwner) {
            viewModel.getTechs(it)
        }

        viewModel.techs.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    viewModel._technicians.value = it.data.technicians

                    if (it.data.technicians == listOf<Technician>()) {
                        tvError.visible(true)
                    }
                    loadingProgressBar.visible(false)
                }
                is Result.Loading -> {
                    loadingProgressBar.visible(true)
                    tvError.visible(false)
                }
                is Result.Error -> {
                    tvError.visible(true)
                    loadingProgressBar.visible(false)
                }
            }
        }
    }
}