package com.example.servizi.user.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.servizi.R
import com.google.android.material.snackbar.Snackbar

class UserHomeFragment : Fragment() {

    private lateinit var viewModel: UserHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Handle the back button event
            Snackbar.make(requireView(), "Confirm Exit App ?", Snackbar.LENGTH_SHORT)
                .setAction("EXIT") {
                    activity?.moveTaskToBack(true);
                    activity?.finish();
                }.show()
        }
        callback.isEnabled = true

        return inflater.inflate(R.layout.user_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserHomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}