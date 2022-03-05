package com.example.servizi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.servizi.databinding.ActivityMainBinding
import com.example.servizi.technician.TechnicianMainActivity
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.UserMainActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //declare val for database
        val userPreferences = UserPreferences(this)

        //hide toolbar
        hideSystemUI()

        //Redirect to User Activity
        binding.btnUser.setOnClickListener {
            lifecycleScope.launch {
                userPreferences.saveUserType("User")
            }
            val intent = Intent(this@MainActivity, UserMainActivity::class.java)
            Toast.makeText(this@MainActivity, "Opining User App", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            //finish()
        }

        //Redirect to Technician Activity
        binding.btnTechnician.setOnClickListener {
            lifecycleScope.launch {
                userPreferences.saveUserType("Tech")
            }
            val intent = Intent(this@MainActivity, TechnicianMainActivity::class.java)
            Toast.makeText(this@MainActivity, "Opining Technician App", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            //finish()
        }

        //check Token
        userPreferences.accessToken.asLiveData().observe(this) {
            if (it == "") {
                binding.MainActivity.visible(true)
            } else {
                userPreferences.usertype.asLiveData().observe(this) { userType ->
                    if (userType == "User") {
                        val intent = Intent(this@MainActivity, UserMainActivity::class.java)
                        Toast.makeText(this@MainActivity, "Opining User App", Toast.LENGTH_SHORT)
                            .show()
                        intent.putExtra("LoggedIn", true)
                        startActivity(intent)
                        finish()
                    } else if (userType == "Tech") {
                        val intent = Intent(this@MainActivity, TechnicianMainActivity::class.java)
                        Toast.makeText(
                            this@MainActivity,
                            "Opining Technician App",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        intent.putExtra("LoggedIn", true)
                        startActivity(intent)
                        finish()
                    }
                }

            }
        }

    }


    //Fun to Hide Status Bar At The Top of The Screen
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}