package com.example.servizi.user.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.servizi.R
import com.example.servizi.application.BaseFragment
import com.example.servizi.databinding.FragmentUserHomeBinding
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.network.UserApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class UserHomeFragment : BaseFragment<UserSharedViewModel, FragmentUserHomeBinding, UserRepository>(),
    View.OnClickListener {

    private val userSharedModel1: UserSharedViewModel by activityViewModels()

    override fun getViewModel() = UserSharedViewModel::class.java

    override fun getFragmentRepository(): UserRepository {

        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return UserRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.carpenterCrdV.setOnClickListener(this)
        binding.plumberCrdV.setOnClickListener(this)
        binding.airConditioningCrdV.setOnClickListener(this)
        binding.electricianCrdV.setOnClickListener(this)
        binding.paintingWorkCrdV.setOnClickListener(this)
        binding.carMechanicCrdV.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.carpenterCrdV -> {
                userSharedModel1._techProf.value = "carpenter"
                findNavController().navigate(R.id.action_navigation_home_to_techniciansFragment)
            }
            R.id.plumberCrdV -> {
                userSharedModel1._techProf.value = "plumber"
                findNavController().navigate(R.id.action_navigation_home_to_techniciansFragment)
            }
            R.id.air_conditioningCrdV -> {
                userSharedModel1._techProf.value = "air conditioning"
                findNavController().navigate(R.id.action_navigation_home_to_techniciansFragment)
            }
            R.id.electricianCrdV -> {
                userSharedModel1._techProf.value = "electrician"
                findNavController().navigate(R.id.action_navigation_home_to_techniciansFragment)
            }
            R.id.painting_workCrdV -> {
                userSharedModel1._techProf.value = "painting"
                findNavController().navigate(R.id.action_navigation_home_to_techniciansFragment)
            }
            R.id.car_mechanicCrdV -> {
                userSharedModel1._techProf.value = "mechanic"
                findNavController().navigate(R.id.action_navigation_home_to_techniciansFragment)
            }
        }
    }
}