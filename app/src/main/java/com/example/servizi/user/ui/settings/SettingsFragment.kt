package com.example.servizi.user.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.servizi.application.BaseFragment
import com.example.servizi.databinding.SettingsFragmentBinding
import com.example.servizi.user.model.TechRepository
import com.example.servizi.user.network.UserApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SettingsFragment :
    BaseFragment<SettingsViewModel, SettingsFragmentBinding, TechRepository>() {
    override fun getViewModel() = SettingsViewModel::class.java

    override fun getFragmentRepository(): TechRepository {
        val token = ""
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return TechRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = SettingsFragmentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener { logout() }
    }

}