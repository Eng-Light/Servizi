package com.example.servizi.user.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.example.servizi.application.BaseFragment
import com.example.servizi.databinding.UserHomeFragmentBinding
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.user.model.TechRepository
import com.example.servizi.user.model.TechniciansResponse
import com.example.servizi.user.network.UserApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class UserHomeFragment : BaseFragment<HomeViewModel, UserHomeFragmentBinding, TechRepository>() {

    private fun updateUI(data: TechniciansResponse) {
        Toast.makeText(
            requireContext(),
            "UserHomeFragment : " + data.technicians.toString(),
            Toast.LENGTH_LONG
        ).show()
    }

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

        viewModel.getTechs("carpenter")

        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    updateUI(it.data)
                }
                is Result.Loading ->
                    Toast.makeText(
                        requireContext(),
                        "UserHomeFragment : Loading",
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
    }
}