package com.example.servizi.user.ui.technichians

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.servizi.R

class TechniciansFragment : Fragment() {

    companion object {
        fun newInstance() = TechniciansFragment()
    }

    private lateinit var viewModel: TechniciansViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.technicians_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TechniciansViewModel::class.java)
        // TODO: Use the ViewModel
    }

}