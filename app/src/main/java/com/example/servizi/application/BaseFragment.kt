package com.example.servizi.application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.servizi.MainActivity
import com.example.servizi.application.BaseRepository
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.ui.login.startNewActivity
import com.example.servizi.user.network.RemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


abstract class BaseFragment<VM : ViewModel, B : ViewBinding, R : BaseRepository> : Fragment() {
    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val remoteDataSource = RemoteDataSource()

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

    fun logout() = lifecycleScope.launch {
        userPreferences.clear()
        requireActivity().startNewActivity(MainActivity::class.java)
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentRepository(): R

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B
}