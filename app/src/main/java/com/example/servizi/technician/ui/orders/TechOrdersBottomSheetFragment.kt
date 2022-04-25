package com.example.servizi.technician.ui.orders

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentTechOrdersBottomSheetBinding
import com.example.servizi.technician.model.Appointment
import com.example.servizi.technician.model.TechRepository
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.network.TechApiService
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.network.RemoteDataSource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
class TechOrdersBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var userPreferences: UserPreferences
    private val remoteDataSource = RemoteDataSource()

    val binding: FragmentTechOrdersBottomSheetBinding by lazy {
        FragmentTechOrdersBottomSheetBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: TechOrdersBottomSheetViewModel

    fun getFragmentRepository(): TechRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(TechApiService::class.java, token)
        return TechRepository(api)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userPreferences = UserPreferences(requireContext())
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory)[TechOrdersBottomSheetViewModel::class.java]
        lifecycleScope.launch { userPreferences.accessToken.first() }

        arguments?.get("order")?.let { it ->
            viewModel.ordersData.value = it as Appointment
        }

        binding.lifecycleOwner = this
        binding.appointment = viewModel.ordersData.value

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.ordersData.observe(viewLifecycleOwner) { it1 ->
            when (it1.status) {
                "accepted" -> {
                    binding.btnAccept.visible(false)
                    binding.btnReject.visible(false)
                    binding.btnCompleted.visible(true)
                }
                "inprogress" -> {
                    binding.btnAccept.visible(true)
                    binding.btnReject.visible(true)
                    binding.btnCompleted.visible(false)
                }
                else -> {
                    binding.btnAccept.visible(false)
                    binding.btnReject.visible(false)
                    binding.btnCompleted.visible(false)
                }
            }
        }

        binding.btnAccept.setOnClickListener {
            viewModel.ordersData.value?.let { it1 ->
                if (it1.status != "accepted") {
                    showAlertDialogAccept()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "The Order is Already Accepted !",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        binding.btnReject.setOnClickListener {
            viewModel.ordersData.value?.let { it1 ->
                if (it1.status != "rejected") {
                    showAlertDialogReject()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "The Order is Already Rejected !",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        binding.btnCompleted.setOnClickListener {
            viewModel.ordersData.value?.let { it1 ->
                if (it1.status != "completed") {
                    showAlertDialogCompleted()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "The Order is Already Completed !",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        viewModel.acceptResponse.observe(viewLifecycleOwner) {
            binding.loading.visible(true)
            when (it) {
                is Result.Loading -> {
                    binding.loading.visible(true)
                }
                is Result.Success -> {
                    binding.loading.visible(false)
                    Toast.makeText(
                        requireContext(),
                        "Order Accepted Successfully !",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    dismiss()
                }
                is Result.Error -> {
                    binding.loading.visible(false)
                    handleApiError(it) {
                        viewModel.acceptOrder(
                            viewModel.ordersData.value?.id!!,
                            "accepted"
                        )
                    }
                }
            }
        }

        viewModel.rejectResponse.observe(viewLifecycleOwner) {
            binding.loading.visible(true)
            when (it) {
                is Result.Loading -> {
                    binding.loading.visible(true)
                }
                is Result.Success -> {
                    binding.loading.visible(false)
                    Toast.makeText(
                        requireContext(),
                        "Order Rejected Successfully !",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    dismiss()
                }
                is Result.Error -> {
                    binding.loading.visible(false)
                    handleApiError(it) {
                        viewModel.rejectOrder(
                            viewModel.ordersData.value?.id!!,
                            "rejected"
                        )
                    }
                }
            }
        }

        viewModel.completeResponse.observe(viewLifecycleOwner) {
            binding.loading.visible(true)
            when (it) {
                is Result.Loading -> {
                    binding.loading.visible(true)
                }
                is Result.Success -> {
                    binding.loading.visible(false)
                    Toast.makeText(
                        requireContext(),
                        "Order Completed Successfully !",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    dismiss()
                }
                is Result.Error -> {
                    binding.loading.visible(false)
                    handleApiError(it) {
                        viewModel.completeOrder(
                            viewModel.ordersData.value?.id!!,
                            "completed"
                        )
                    }
                }
            }
        }
    }

    private fun showAlertDialogCompleted() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure you want to mark this order as completed ?")
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.completeOrder(
                viewModel.ordersData.value?.id!!,
                "completed"
            )
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertDialogReject() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Servizi")
            .setMessage("Are You Sure Rejecting Order ?")
            .setPositiveButton(
                "Confirm"
            ) { _, _ ->
                viewModel.ordersData.value?.let { it1 ->
                    viewModel.rejectOrder(
                        it1.id,
                        "rejected"
                    )
                }
            }
            .setNegativeButton(
                "Keep Order"
            ) { _, _ ->
                Toast.makeText(requireContext(), "Nice Choice !", Toast.LENGTH_SHORT).show()
            }.show()
    }

    private fun showAlertDialogAccept() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Servizi")
            .setMessage("Accept Order ?")
            .setPositiveButton(
                "Confirm"
            ) { _, _ ->
                viewModel.ordersData.value?.let { it1 ->
                    viewModel.acceptOrder(
                        it1.id,
                        "accepted"
                    )
                }
            }
            .setNegativeButton(
                "no"
            ) { _, _ ->
                dismiss()
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy","onDestroy")
        viewModel.completeResponse.removeObservers(this)
        viewModel.rejectResponse.removeObservers(this)
        viewModel.acceptResponse.removeObservers(this)
    }

}