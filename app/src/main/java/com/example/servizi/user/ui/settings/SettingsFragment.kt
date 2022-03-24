package com.example.servizi.user.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.servizi.application.BaseFragment
import com.example.servizi.databinding.SettingsFragmentBinding
import com.example.servizi.user.model.TechRepository
import com.example.servizi.user.network.UserApiService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

        binding.logoutCrdV.setOnClickListener { showAlertDialog() }
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Servizi")
            .setMessage("Confirm logout ?")
            .setPositiveButton(
                "Logout"
            ) { _, _ ->
                logout()
            }
            .setNegativeButton(
                "Stay"
            ) { _, _ ->
                Toast.makeText(requireContext(), "Nice Choice !", Toast.LENGTH_SHORT).show()
            }.show()
    }
}