package com.example.servizi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //handle splash screen
        var flag = true
        installSplashScreen().setKeepOnScreenCondition {

            Handler().postDelayed({
                flag = false
            }, 2000)

            flag
        }
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
        checkToken(userPreferences)
    }

    private fun checkToken(userPreferences: UserPreferences) {
        userPreferences.accessToken.asLiveData().observe(this) {
            if (it == "") {
                binding.MainActivity.visible(true)
            } else {
                userPreferences.expiration.asLiveData().observe(this) { it1 ->
                    if (it1 == "") {
                        checkToken(userPreferences)
                    } else {
                        if (validateDate(it1)) {
                            userPreferences.usertype.asLiveData().observe(this) { userType ->
                                if (userType == "User") {
                                    val intent =
                                        Intent(this@MainActivity, UserMainActivity::class.java)
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Opining User App",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    intent.putExtra("LoggedIn", true)
                                    startActivity(intent)
                                    finish()
                                } else if (userType == "Tech") {
                                    val intent =
                                        Intent(
                                            this@MainActivity,
                                            TechnicianMainActivity::class.java
                                        )
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
                        } else {
                            showAlertDialog(userPreferences)
                        }
                    }
                }
            }
        }
    }

    private fun performLogout(userPreferences: UserPreferences) = lifecycleScope.launch {
        userPreferences.clear()
    }

    private fun showAlertDialog(userPreferences: UserPreferences) {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setTitle("Servizi")
            .setMessage("\nLogin Expired! \n\nPlease Login Again ")
            .setPositiveButton(
                "Ok"
            ) { _, _ ->
                performLogout(userPreferences)
            }.show()
    }

    private fun validateDate(exDate: String?): Boolean {
        val exD = Calendar.getInstance()
        exD.time = sdf.parse(exDate!!)!!
        val curD = Calendar.getInstance()
        return curD <= exD
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