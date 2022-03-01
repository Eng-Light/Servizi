package com.example.servizi.user

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.servizi.R
import com.example.servizi.databinding.ActivityUserMainBinding

class UserMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityUserMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar2)

        val navController = findNavController(R.id.nav_host_fragment_content_user_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.PagerFragment -> {
                    supportActionBar?.hide()
                    binding.toolbar2.visibility = View.GONE
                }
                else -> {
                    supportActionBar?.show()
                    binding.toolbar2.visibility = View.VISIBLE
                }
            }
        }
            }

            override fun onSupportNavigateUp(): Boolean {
                val navController = findNavController(R.id.nav_host_fragment_content_user_main)
                return navController.navigateUp(appBarConfiguration)
                        || super.onSupportNavigateUp()
            }
        }

