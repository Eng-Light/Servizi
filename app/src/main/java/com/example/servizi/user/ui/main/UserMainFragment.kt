package com.example.servizi.user.ui.main

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
import com.example.servizi.databinding.FragmentUserMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class UserMainFragment : Fragment() {

    private lateinit var viewModel: UserMainViewModel
    private var _binding: FragmentUserMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserMainBinding.inflate(inflater, container, false)


        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showAlertDialog()
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

        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.user_home_fragment, R.id.techniciansFragment,R.id.UserHomeFragment
            )
        )*/
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)

        /*(navHostFragment.requireActivity() as AppCompatActivity).setupActionBarWithNavController(
            navController,
            appBarConfiguration
        )*/

        // hide action bar
        (navHostFragment.requireActivity() as AppCompatActivity).supportActionBar?.hide()
        /*navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.techniciansFragment -> {
                    (navHostFragment.requireActivity() as AppCompatActivity).supportActionBar?.show()
                    //(navHostFragment.requireActivity() as AppCompatActivity).supportActionBar?.title = "Carpenter"
                }
                else -> {
                    (navHostFragment.requireActivity() as AppCompatActivity).supportActionBar?.hide()
                }
            }
        }*/
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