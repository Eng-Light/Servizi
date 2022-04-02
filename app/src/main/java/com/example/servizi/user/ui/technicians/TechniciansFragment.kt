package com.example.servizi.user.ui.technicians

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.servizi.R
import com.example.servizi.application.BaseFragment
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentUserTechniciansBinding
import com.example.servizi.databinding.PopupUpdateLocationBinding
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.model.NewLocation
import com.example.servizi.user.model.Technician
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.network.UserApiService
import com.example.servizi.user.ui.home.UserSharedViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TechniciansFragment :
    BaseFragment<TechniciansViewModel, FragmentUserTechniciansBinding, UserRepository>() {

    private var popupWindow: PopupWindow? = null
    private var _popBinding: PopupUpdateLocationBinding? = null
    private var newLocation: NewLocation? = null
    private val popBinding get() = _popBinding!!

    private val userSharedModel: UserSharedViewModel by activityViewModels()

    override fun getViewModel() = TechniciansViewModel::class.java

    override fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return UserRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserTechniciansBinding.inflate(inflater, container, false)

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

        refreshApp()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingProgressBar = binding.loading
        val tvError = binding.tvError
        val nLocation = NewLocation("", "")

        lifecycleScope.launch {
            userPreferences.city.asLiveData().observe(viewLifecycleOwner) {
                if (it != null) {
                    nLocation.city = it
                }
                viewModel.setLocation(nLocation)
            }
        }

        userSharedModel.techProf.observe(viewLifecycleOwner) {
            viewModel.setProfession(it)
            viewModel.getTechs(it)
        }

        viewModel.techs.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    viewModel._technicians.value = it.data.technicians

                    if (it.data.technicians == listOf<Technician>()) {
                        tvError.visible(true)
                        binding.rvShowData.visible(false)
                    } else {
                        binding.rvShowData.visible(true)
                    }
                    loadingProgressBar.visible(false)
                }
                is Result.Loading -> {
                    loadingProgressBar.visible(true)
                    tvError.visible(false)
                }
                is Result.Error -> {
                    handleApiError(it)
                    binding.rvShowData.visible(false)
                    tvError.visible(true)
                    loadingProgressBar.visible(false)
                }
            }
        }

        viewModel.updateResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    loadingProgressBar.visible(false)
                    Toast.makeText(requireContext(), "Address Updated", Toast.LENGTH_SHORT)
                        .show()
                    userSharedModel.techProf.value?.let { it1 -> viewModel.getTechs(it1) }
                    lifecycleScope.launch {
                        userPreferences.saveUserGovernorate(newLocation?.governorate)
                        userPreferences.saveUserCity(newLocation?.city)
                    }
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

        binding.tvLocation.setOnClickListener {
            dismissPopup()
            popupWindow = showPopUpUpdateLoc()
            popupWindow?.isOutsideTouchable = true
            popupWindow?.isFocusable = true
            popupWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow?.showAsDropDown(binding.tvLocation)
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

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            userSharedModel.techProf.value?.let { it -> viewModel.getTechs(it) }
            binding.swipeToRefresh.isRefreshing = false
        }
    }
}