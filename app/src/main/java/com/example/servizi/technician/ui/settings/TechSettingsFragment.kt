package com.example.servizi.technician.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.servizi.R
import com.example.servizi.application.BaseFragment
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentTechSettingsBinding
import com.example.servizi.databinding.PopupUpdateLocationBinding
import com.example.servizi.technician.model.TechRepository
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.network.TechApiService
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.model.NewLocation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TechSettingsFragment :
    BaseFragment<TechSettingsViewModel, FragmentTechSettingsBinding, TechRepository>() {

    private var popupWindow: PopupWindow? = null
    private var _popBinding: PopupUpdateLocationBinding? = null
    private var newLocation: NewLocation? = null
    private val popBinding get() = _popBinding!!

    override fun getViewModel() = TechSettingsViewModel::class.java

    override fun getFragmentRepository(): TechRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(TechApiService::class.java, token)
        return TechRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTechSettingsBinding.inflate(inflater, container, false)

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingProgressBar = binding.loading
       binding.accountCrdV.setOnClickListener {
           findNavController().navigate(R.id.action_navigation_tech_settings_to_navigation_update_tech_profile)
            }
        binding.logoutCrdV.setOnClickListener { showAlertDialog() }

        viewModel.updateResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        loadingProgressBar.visible(false)
                        Toast.makeText(requireContext(), "Address Updated", Toast.LENGTH_SHORT)
                            .show()
                        lifecycleScope.launch {
                            userPreferences.saveUserGovernorate(newLocation?.governorate)
                            userPreferences.saveUserCity(newLocation?.city)
                        }
                        viewModel._updateResponse.value = null
                    }
                    is Result.Loading -> {
                        loadingProgressBar.visible(true)
                    }
                    is Result.Error -> {
                        handleApiError(it) {
                            viewModel.updateLoc(newLocation!!)
                        }
                        loadingProgressBar.visible(false)
                    }
                }
            }
        }

        binding.shareAppCrdV.setOnClickListener {
            shareApp()
        }
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

    private fun shareApp() {

        val shareString =
            "Download Servizi for a flawless, safe and professional maintenance and finishing experience"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, "Servizi")
            putExtra(
                Intent.EXTRA_TEXT,
                "$shareString \n https://drive.google.com/file/d/1-91e35hrnkNsnIOmeYA3_teSw6nnq5Hm/view?usp=sharing"
            )
        }

        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            val shareIntent = Intent.createChooser(intent, "Share with")
            startActivity(shareIntent)
        }
    }

}