package com.example.servizi.technician.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.servizi.R

class TechProfileFragment : Fragment() {

    companion object {
        fun newInstance() = TechProfileFragment()
    }

    private lateinit var viewModel: TechProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tech_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TechProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}