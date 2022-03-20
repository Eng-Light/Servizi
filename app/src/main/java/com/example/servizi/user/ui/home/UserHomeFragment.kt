package com.example.servizi.user.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.servizi.R
import com.example.servizi.application.BaseFragment
import com.example.servizi.databinding.UserHomeFragmentBinding
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.TechRepository
import com.example.servizi.user.model.TechniciansResponse
import com.example.servizi.user.network.UserApiService
import com.example.servizi.user.ui.technicians.TechniciansViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class UserHomeFragment : BaseFragment<HomeViewModel, UserHomeFragmentBinding, TechRepository>(),
    View.OnClickListener {

    private val viewModel2: TechniciansViewModel by activityViewModels()

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentRepository(): TechRepository {

        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return TechRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = UserHomeFragmentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.techs.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    sendData(it.data)
                    viewModel2._technicians.value = it.data.technicians
                    findNavController().navigate(R.id.action_navigation_home_to_techniciansFragment)
                }
                is Result.Loading -> {
                    Toast.makeText(
                        requireContext(),
                        "Loading ...",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error :( ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.carpenterCrdV.setOnClickListener(this)
        binding.plumberCrdV.setOnClickListener(this)
        binding.airConditioningCrdV.setOnClickListener(this)
        binding.electricianCrdV.setOnClickListener(this)
        binding.paintingWorkCrdV.setOnClickListener(this)
        binding.carMechanicCrdV.setOnClickListener(this)
    }

    private fun sendData(data: TechniciansResponse) {
        Toast.makeText(
            requireContext(),
            "Success :) " + data.technicians,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun getTechs(prof: String) {
        viewModel.getTechs(prof)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.carpenterCrdV -> {
                getTechs("carpenter")
            }
            R.id.plumberCrdV -> {
                getTechs("plumber")
            }
            R.id.air_conditioningCrdV -> {
                getTechs("air conditioning")
            }
            R.id.electricianCrdV -> {
                getTechs("electrician")
            }
            R.id.painting_workCrdV -> {
                getTechs("painting")
            }
            R.id.car_mechanicCrdV -> {
                getTechs("mechanic")
            }
        }
    }
}