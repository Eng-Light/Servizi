package com.example.servizi.technician.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.servizi.R
import com.example.servizi.databinding.FragmentTechMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class TechMainFragment : Fragment() {

    private lateinit var viewModel: TechMainViewModel
    private var _binding: FragmentTechMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTechMainBinding.inflate(inflater, container, false)
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Handle the back button event
            showAlertDialog()
        }
        callback.isEnabled = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TechMainViewModel::class.java]
        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment_tech_main) as NavHostFragment

        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)

        // hide action bar
        (navHostFragment.requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Servizi")
            .setMessage("Confirm Exit Application ?")
            .setPositiveButton(
                "Exit"
            ) { _, _ ->
                activity?.moveTaskToBack(true)
                activity?.finish()
            }
            .setNegativeButton(
                "Stay"
            ) { _, _ ->
                Snackbar.make(requireView(), "Canceled", Snackbar.LENGTH_SHORT)
                    .show()
            }.show()
    }

}