package com.example.servizi.technician

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.servizi.R
import com.example.servizi.databinding.ActivityTechnicianMainBinding

class TechnicianMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTechnicianMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTechnicianMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_technician_main)
        val loggedIn = intent.extras
        if (loggedIn != null) {
            if (loggedIn.getBoolean("LoggedIn")) {
                navController.navigate(R.id.techHomeFragment)
            }
        }
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.TechnicianPagerFragment,
                R.id.techHomeFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.TechnicianPagerFragment -> {
                    supportActionBar?.hide()
                    binding.toolbar.visibility = View.GONE
                }
                else -> {
                    supportActionBar?.show()
                    binding.toolbar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_technician_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}