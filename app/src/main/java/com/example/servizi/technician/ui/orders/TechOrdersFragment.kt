package com.example.servizi.technician.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.servizi.R
import com.example.servizi.application.BaseFragment
import com.example.servizi.application.BaseRepository
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentTechOrdersBinding
import com.example.servizi.technician.model.TechRepository
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.network.TechApiService
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.ui.my_orders.StatusAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TechOrdersFragment :
    BaseFragment<TechOrdersViewModel, FragmentTechOrdersBinding, BaseRepository>() {

    private var selectedStatus: Int? = 0

    override fun getViewModel() = TechOrdersViewModel::class.java

    override fun getFragmentRepository(): TechRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(TechApiService::class.java, token)
        return TechRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTechOrdersBinding.inflate(inflater, container, false)

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
        binding.ordersViewModel = viewModel

        val ordersAdapter = TechOrdersAdapter()
        ordersAdapter.onItemClick = {
            val bundle = bundleOf("order" to (it as Any))
            findNavController().navigate(
                R.id.action_navigation_tech_orders_to_techOrdersBottomSheetFragment,
                bundle
            )
        }
        binding.rvOrders.adapter = ordersAdapter

        refreshApp()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.ordersResponse.value == null) {
            viewModel.gerOrders()
        }

        viewModel.ordersResponse.observe(viewLifecycleOwner) {
            binding.loading.visible(true)

            when (it) {
                is Result.Success -> {
                    binding.loading.visible(false)
                    viewModel.setOrdersData(it.data)
                    viewModel.sortOrders()
                    viewModel.setSortedOrders(selectedStatus!!)
                }
                is Result.Loading -> {
                    binding.loading.visible(true)
                }
                is Result.Error -> {
                    binding.loading.visible(false)
                    handleApiError(it) {
                        viewModel.gerOrders()
                    }
                }
            }
        }

        val adapter = StatusAdapter()
        adapter.submitList(
            arrayListOf(
                "inprogress", "accepted", "rejected", "cancelled", "completed"
            )
        )
        adapter.onItemClick = {
            selectedStatus = it
            viewModel.setSortedOrders(selectedStatus!!)
        }
        binding.rvStatus.adapter = adapter
        viewModel = ViewModelProvider(this)[TechOrdersViewModel::class.java]

    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.gerOrders()
            binding.swipeToRefresh.isRefreshing = false
        }
    }
}