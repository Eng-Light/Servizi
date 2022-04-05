package com.example.servizi.user.ui.my_orders

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.servizi.R
import com.example.servizi.application.BaseFragment
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentUserMyOrdersBinding
import com.example.servizi.databinding.PopupCancelOrderBinding
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.model.Appointment
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.network.UserApiService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MyOrdersFragment :
    BaseFragment<MyOrdersViewModel, FragmentUserMyOrdersBinding, UserRepository>() {

    private var popupWindow: PopupWindow? = null
    private var _popBinding: PopupCancelOrderBinding? = null
    private var appointment: Appointment? = null
    private val popBinding get() = _popBinding!!

    override fun getViewModel() = MyOrdersViewModel::class.java

    override fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return UserRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserMyOrdersBinding.inflate(inflater, container, false)

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
        val ordersAdapter = OrdersAdapter()
        ordersAdapter.onItemClick = {
            appointment = it
            showPopUpUpdateLoc()
            popupWindow = showPopUpUpdateLoc()
            popupWindow?.isOutsideTouchable = true
            popupWindow?.isFocusable = true
            popupWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow?.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
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
            binding.loading.visible(false)

            when (it) {
                is Result.Success -> {
                    binding.loading.visible(false)
                    viewModel.setOrdersData(it.data)
                    viewModel.sortOrders()
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

        viewModel.cancelResponse.observe(viewLifecycleOwner) {
            binding.loading.visible(false)

            when (it) {
                is Result.Success -> {
                    binding.loading.visible(false)
                }
                is Result.Loading -> {
                    binding.loading.visible(true)
                }
                is Result.Error -> {
                    binding.loading.visible(false)
                    handleApiError(it) {
                        viewModel.cancelApp(appointment!!.id)
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
        adapter.onItemClick = { viewModel.setSortedOrders(it) }
        binding.rvStatus.adapter = adapter
        viewModel = ViewModelProvider(this)[MyOrdersViewModel::class.java]

    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.gerOrders()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun showPopUpUpdateLoc(): PopupWindow {
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_cancel_order, null)
        _popBinding = PopupCancelOrderBinding.bind(view)
        _popBinding!!.tech = appointment!!.technician
        _popBinding!!.appointment = appointment

        popBinding.button.setOnClickListener {
            if (appointment!!.status != "cancelled") {
                showAlertDialog(appointment!!.id)
            } else {
                Toast.makeText(
                    requireContext(),
                    "The Order is Already Cancelled !",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
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

    private fun showAlertDialog(id: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Servizi")
            .setMessage("Are You Sure Canceling Order ?")
            .setPositiveButton(
                "Confirm"
            ) { _, _ ->
                viewModel.cancelApp(id)
            }
            .setNegativeButton(
                "Keep Order"
            ) { _, _ ->
                Toast.makeText(requireContext(), "Nice Choice !", Toast.LENGTH_SHORT).show()
            }.show()
    }
}