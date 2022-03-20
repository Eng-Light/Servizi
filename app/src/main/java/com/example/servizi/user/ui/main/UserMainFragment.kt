package com.example.servizi.user.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.servizi.R
import com.example.servizi.databinding.FragmentTechnicianSignUpBinding
import com.example.servizi.databinding.UserMainFragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class UserMainFragment : Fragment() {

    private lateinit var viewModel: UserMainViewModel
    private var _binding: UserMainFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = UserMainFragmentBinding.inflate(inflater, container, false)


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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UserMainViewModel::class.java]

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment

        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)

        // hide action bar
        (navHostFragment.requireActivity() as AppCompatActivity).supportActionBar?.hide()

    }

}