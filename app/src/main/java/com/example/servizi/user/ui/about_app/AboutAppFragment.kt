package com.example.servizi.user.ui.about_app

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.servizi.R

class AboutAppFragment : Fragment() {

    companion object {
        fun newInstance() = AboutAppFragment()
    }

    private lateinit var viewModel: AboutAppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AboutAppViewModel::class.java)
        // TODO: Use the ViewModel
    }

}