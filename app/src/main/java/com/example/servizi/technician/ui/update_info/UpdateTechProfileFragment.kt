package com.example.servizi.technician.ui.update_info

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import com.example.servizi.R
import com.example.servizi.application.BaseFragment
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentTechUpdateProfileBinding
import com.example.servizi.technician.model.TechRepository
import com.example.servizi.technician.model.UpdateRequest
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.network.TechApiService
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UpdateTechProfileFragment :
    BaseFragment<UpdateTechProfileViewModel, FragmentTechUpdateProfileBinding, TechRepository>() {

    private var newData: UpdateRequest? = null
    override fun getFragmentRepository(): TechRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(TechApiService::class.java, token)
        return TechRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTechUpdateProfileBinding.inflate(inflater, container, false)

    override fun getViewModel() = UpdateTechProfileViewModel::class.java

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        binding.editGovernorate.adapter = arrayAdapterGov

        val arrayAdapterCities = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            suez
        )
        binding.editCity.adapter = arrayAdapterCities

        binding.editGovernorate.onItemSelectedListener =
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
                            binding.editCity.adapter = arrayAdapterCity
                        }
                        1 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                cairo
                            )
                            binding.editCity.adapter = arrayAdapterCity
                        }
                        2 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                alexandria
                            )
                            binding.editCity.adapter = arrayAdapterCity
                        }
                        3 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                qalyubia
                            )
                            binding.editCity.adapter = arrayAdapterCity
                        }
                        4 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                giza
                            )
                            binding.editCity.adapter = arrayAdapterCity
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.updateInfo.setOnClickListener {
            showAlertDialog()
        }

        viewModel.updateinfoResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    binding.loading.visible(true)
                }
                is Result.Success -> {
                    binding.loading.visible(false)
                }
                is Result.Error -> {
                    binding.loading.visible(false)
                    handleApiError(it) {
                        viewModel.updateinfo(newData!!)
                    }
                }
            }
        }
    }

    private fun validate(data: UpdateRequest): Boolean {
        var valid = true

        if (!stringValidator(data.firstName)) {
            binding.fName.error = "Not Valid Name"
            valid = false
        }
        if (!stringValidator(data.lastName)) {
            binding.lName.error = "Not Valid Name"
            valid = false
        }
        return valid
    }

    private fun stringValidator(_name: String): Boolean {
        return !(_name.isEmpty() || _name.length < 3)
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Servizi")
        builder.setMessage("Confirm change ?")
        builder.setPositiveButton("Yes") { _, _ ->
            newData = UpdateRequest(
                binding.fName.text.toString().trim(),
                binding.lName.text.toString().trim(),
                binding.editGovernorate.selectedItem.toString().trim(),
                binding.editCity.selectedItem.toString().trim()
            )
            if (validate(newData!!)) {
                viewModel.updateinfo(newData!!)
            } else {
                Snackbar.make(requireView(), "Please Chick Your Data", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
