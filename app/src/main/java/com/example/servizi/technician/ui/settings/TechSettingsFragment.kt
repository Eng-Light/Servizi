package com.example.servizi.technician.ui.settings

import android.content.Context
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

        /*binding.changeLocCrdV.setOnClickListener {
            showPopUpUpdateLoc()
            popupWindow = showPopUpUpdateLoc()
            popupWindow?.isOutsideTouchable = true
            popupWindow?.isFocusable = true
            popupWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow?.showAsDropDown(binding.changeLocCrdV)
        }*/

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
    }

    private fun showPopUpUpdateLoc(): PopupWindow {
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_update_location, null)
        _popBinding = PopupUpdateLocationBinding.bind(view)

        val governorates =
            requireContext().resources.getStringArray(R.array.Governorate_List)
        val suez =
            requireContext().resources.getStringArray(R.array.Suez_Cities)
        val cairo =
            requireContext().resources.getStringArray(R.array.Cairo_Cities)
        val alexandria =
            requireContext().resources.getStringArray(R.array.Alexandria_Cities)
        val qalyubia =
            requireContext().resources.getStringArray(R.array.Qalyubia_Cities)
        val giza =
            requireContext().resources.getStringArray(R.array.Giza_Cities)

        val arrayAdapterGov = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            governorates
        )
        popBinding.yourGovernorate.adapter = arrayAdapterGov

        val arrayAdapterCities = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            suez
        )
        popBinding.yourCity.adapter = arrayAdapterCities

        popBinding.yourGovernorate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val arrayAdapterCity: ArrayAdapter<String>
                    when (p2) {
                        0 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                suez
                            )
                            popBinding.yourCity.adapter = arrayAdapterCity
                        }
                        1 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                cairo
                            )
                            popBinding.yourCity.adapter = arrayAdapterCity
                        }
                        2 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                alexandria
                            )
                            popBinding.yourCity.adapter = arrayAdapterCity
                        }
                        3 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                qalyubia
                            )
                            popBinding.yourCity.adapter = arrayAdapterCity
                        }
                        4 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                giza
                            )
                            popBinding.yourCity.adapter = arrayAdapterCity
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        popBinding.yourBtnUpdate.setOnClickListener {
            newLocation = NewLocation(
                popBinding.yourGovernorate.selectedItem.toString().trim(),
                popBinding.yourCity.selectedItem.toString().trim()
            )
            viewModel.updateLoc(newLocation!!)
            dismissPopup()
        }

        return PopupWindow(
            popBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun dismissPopup() {
        popupWindow?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            popupWindow = null
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

}