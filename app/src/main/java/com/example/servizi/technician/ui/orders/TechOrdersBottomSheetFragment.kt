package com.example.servizi.technician.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.databinding.FragmentTechOrdersBottomSheetBinding
import com.example.servizi.technician.model.Appointment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TechOrdersBottomSheetFragment : BottomSheetDialogFragment() {

    val binding: FragmentTechOrdersBottomSheetBinding by lazy {
        FragmentTechOrdersBottomSheetBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: TechOrdersBottomSheetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[TechOrdersBottomSheetViewModel::class.java]
        arguments?.get("order")?.let {
            viewModel.ordersData.value = it as Appointment
        }

        binding.lifecycleOwner = this
        binding.appointment = viewModel.ordersData.value

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}