package com.example.servizi.user.ui.signup

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
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

        binding.yourBtnRegister.setOnClickListener {

            val userData = UserData(
                binding.yourFirstName.text.toString(),
                binding.yourLastName.text.toString(),
                binding.yourEmail.text.toString(),
                binding.yourPhone.text.toString(),
                binding.yourCity.selectedItem.toString(),
                binding.yourGovernorate.selectedItem.toString(),
                binding.createYourPassword.text.toString()
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
        return if (_phone.isEmpty() || _phone.length < 11) {
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