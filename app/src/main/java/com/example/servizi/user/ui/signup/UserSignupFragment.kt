package com.example.servizi.user.ui.signup

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.R
import com.example.servizi.databinding.FragmentUserSignUpBinding
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.technician.ui.login_signup_pager.PagerViewModel
import com.example.servizi.user.model.UserData
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

class UserSignupFragment : Fragment() {
    private lateinit var viewModel: UserSignUpViewModel
    private val signUpPagerViewModel: PagerViewModel by activityViewModels()
    private var _binding: FragmentUserSignUpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[UserSignUpViewModel::class.java]
        val loadingProgressBar = binding.loading

        val governorates =
            requireContext().resources.getStringArray(R.array.Governorate_List)
        val suez =
            requireContext().resources.getStringArray(R.array.Suez_Cities)
        val cairo =
            requireContext().resources.getStringArray(R.array.Cairo_Cities)
        val alexandria =
            requireContext().resources.getStringArray(R.array.Alexandria_Cities)
        val qalyubia =
            requireContext().resources.getStringArray(R.array.Qalyubia_Cities)
        val giza =
            requireContext().resources.getStringArray(R.array.Giza_Cities)

        val arrayAdapterGov = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            governorates
        )
        binding.yourGovernorate.adapter = arrayAdapterGov

        val arrayAdapterCities = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            suez
        )
        binding.yourCity.adapter = arrayAdapterCities

        binding.yourGovernorate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val arrayAdapterCity: ArrayAdapter<String>
                    when (p2) {
                        0 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                suez
                            )
                            binding.yourCity.adapter = arrayAdapterCity
                        }
                        1 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                cairo
                            )
                            binding.yourCity.adapter = arrayAdapterCity
                        }
                        2 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                alexandria
                            )
                            binding.yourCity.adapter = arrayAdapterCity
                        }
                        3 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                qalyubia
                            )
                            binding.yourCity.adapter = arrayAdapterCity
                        }
                        4 -> {
                            arrayAdapterCity = ArrayAdapter(
                                requireContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                giza
                            )
                            binding.yourCity.adapter = arrayAdapterCity
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.yourBtnRegister.setOnClickListener {

            val userData = UserData(
                binding.yourFirstName.text.toString().trim(),
                binding.yourLastName.text.toString().trim(),
                binding.yourEmail.text.toString().trim(),
                binding.yourPhone.text.toString().trim(),
                binding.yourCity.selectedItem.toString().trim(),
                binding.yourGovernorate.selectedItem.toString().trim(),
                binding.createYourPassword.text.toString().trim()
            )

            Log.d("Test_SignUp", userData.toString())

            if (validate(userData)) {

                viewModel.setUserData(userData)
                viewModel.userSignUpData.value?.let { it1 -> viewModel.signUpUser(it1) }
                swipeToLoginFragment(0)

            } else {
                Snackbar.make(view, "Please Chick SignUp Data", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        binding.login.setOnClickListener {
            swipeToLoginFragment(0)
        }



        viewModel.userSignUpLoadingStatus.observe(viewLifecycleOwner) {
            id.let {

                when (viewModel.userSignUpLoadingStatus.value) {
                    UserSignUpApiStatus.LOADING -> {
                        loadingProgressBar.visible(true)
                    }
                    UserSignUpApiStatus.DONE -> {
                        loadingProgressBar.visible(false)
                        cleanForm()
                        Toast.makeText(
                            context,
                            viewModel.userSignUpResponseData.value?.msg.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    UserSignUpApiStatus.ERROR -> {

                        loadingProgressBar.visible(false)
                        Toast.makeText(
                            context,
                            viewModel.errorMessage.value?.msg.toString() +
                                    "\n ${viewModel.errorMessage.value?.data?.get(0)?.value.toString()}" +
                                    "\n ${viewModel.errorMessage.value?.data?.get(0)?.msg.toString()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    private fun validate(user: UserData): Boolean {
        var valid = true

        if (!stringValidator(user.firstName)) {
            binding.yourFirstName.error = "Not Valid Name"
            valid = false
        }
        if (!stringValidator(user.lastName)) {
            binding.yourLastName.error = "Not Valid Name"
            valid = false
        }
        if (!emailValidator(user.email)) {
            binding.yourEmail.error = "Not Valid E-Mail"
            valid = false
        }
        if (!phoneValidator(user.phone)) {
            binding.yourPhone.error = "Not Valid Phone"
            valid = false
        }
        if (!passwordValidation(user.password)) {
            binding.createYourPassword.error = "Not Valid Password"
            valid = false
        }
        if (binding.repeatYourPassword.text.toString() != binding.createYourPassword.text.toString()) {
            binding.repeatYourPassword.error = "Passwords Mismatched"
            valid = false
        }
        return valid
    }

    private fun cleanForm() {
        binding.yourFirstName.text = null
        binding.yourLastName.text = null
        binding.yourEmail.text = null
        binding.yourPhone.text = null
        binding.createYourPassword.text = null
    }

    private fun emailValidator(_email: String): Boolean {
        return if (_email.isEmpty() || _email.length < 3) {
            false
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(_email).matches()
        }
    }

    private fun phoneValidator(_phone: String): Boolean {
        return if (_phone.isEmpty() || _phone.length != 11) {
            false
        } else {
            Patterns.PHONE.matcher(_phone).matches()
        }
    }

    private fun stringValidator(_name: String): Boolean {
        return if (_name.isEmpty() || _name.length < 3) {
            false
        } else {
            val pattern: Pattern
            val name = "^[a-zA-Z\\\\\\\\s]+"
            pattern = Pattern.compile(name)
            _name.split(" ").all { pattern.matcher(it).matches() }
            /*val matcher: Matcher = pattern.matcher(_name)
            matcher.matches()*/
        }
    }

    private fun passwordValidation(_password: String): Boolean {
        return !(_password.isEmpty() || _password.length < 8)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Swipe to login page
    private fun swipeToLoginFragment(item: Int) {
        signUpPagerViewModel.setPagerSelectedItem(item)
        //(Fragment() as TechnicianPagerFragment).setCurrentItem(0)
    }

}