package com.example.servizi.technician.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.servizi.R
import com.example.servizi.databinding.FragmentLoginBinding
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LoginFragment : Fragment() {

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    //assign property of type UserPreferences
    private lateinit var userPreferences: UserPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize userPreferences whit required context (The Activity which we called the fragment from)
        userPreferences = UserPreferences(requireContext())

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        val userPhoneEditText = binding.userphone
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.userPhoneError?.let {
                    userPhoneEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })


        loginViewModel.loginResponse.observe(
            viewLifecycleOwner
        ) {
            loadingProgressBar.visible(it is Result.Loading)
            when (it) {
                is Result.Success -> {

                    lifecycleScope.launch {
                        userPreferences.saveAccessToken(it.data.userToken)
                        userPreferences.saveTokenExpire(expiration = getExDate(it.data.userExpiresIn?.toIntOrNull())!!)
                        userPreferences.saveUserId(it.data.userId.toString())
                        userPreferences.saveUserGovernorate(it.data.governorate)
                        userPreferences.saveUserCity(it.data.city)
                        Log.d(
                            "Login_UserType_ex2",
                            getExDate(it.data.userExpiresIn?.toIntOrNull())!!
                        )
                    }
                    Log.d("Login_UserType", "UnDefined")

/*                    userPreferences.usertype.asLiveData().value.let { userType ->
                        Log.d("Login_UserType", userType.toString())
                        if (userType == "User") {
                            Log.d("Login_UserType", "User")
                            findNavController().navigate(R.id.action_UserPagerFragment_to_UserHomeFragment)
                        } else if (userType == "Tech") {
                            Log.d("Login_UserType", "Tech")
                            findNavController().navigate(R.id.action_TechnicianPagerFragment_to_techHomeFragment)
                        }
                    }*/
                    userPreferences.usertype.asLiveData().observe(viewLifecycleOwner) { it1 ->
                        if (it1 == "User") {
                            Log.d("Login_UserType", "User")
                            findNavController().navigate(R.id.UserMainFragment)
                        } else if (it1 == "Tech") {
                            Log.d("Login_UserType", "Tech")
                            findNavController().navigate(R.id.techHomeFragment)
                        }
                    }
                    Toast.makeText(
                        this.context,
                        "Logged In Successfully ${it.data.userId}",
                        Toast.LENGTH_LONG
                    ).show()
                    //updateUiWithUser(LoggedInUserView(loginViewModel))
                }
                is Result.Error -> {
                    handleApiError(it) {
                        loginViewModel.login(
                            userPhoneEditText.text.toString().trim(),
                            passwordEditText.text.toString().trim()
                        )
                    }
                    //updateUiWithUser()
                }
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    userPhoneEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        userPhoneEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadingProgressBar.visibility = View.VISIBLE
                loginViewModel.login(
                    userPhoneEditText.text.toString().trim(),
                    passwordEditText.text.toString().trim()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                userPhoneEditText.text.toString().trim(),
                passwordEditText.text.toString().trim()
            )
        }
    }

    private fun getExDate(interval: Int?): String? {
        val cal = Calendar.getInstance()
        if (interval != null) {
            cal.add(Calendar.HOUR_OF_DAY, interval)
        }
        return sdf.format(cal.time).toString()
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayId.toString()
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}