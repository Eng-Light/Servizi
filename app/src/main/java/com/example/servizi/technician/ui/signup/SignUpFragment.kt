package com.example.servizi.technician.ui.signup

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.R
import com.example.servizi.databinding.FragmentTechnicianSignUpBinding
import com.example.servizi.databinding.PopupEnterDateBinding
import com.example.servizi.technician.model.signup.TechnicianData
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.technician.ui.login_signup_pager.PagerViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * A placeholder fragment containing a simple view.
 */
class SignUpFragment : Fragment() {

    private lateinit var viewModel: SignUpViewModel
    private val signUpPagerViewModel: PagerViewModel by activityViewModels()
    private var _binding: FragmentTechnicianSignUpBinding? = null
    private var popupWindow: PopupWindow? = null
    private var _popBinding: PopupEnterDateBinding? = null
    private val popBinding get() = _popBinding!!

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTechnicianSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
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


        binding.btnRegister.setOnClickListener {

            val techData: TechnicianData
            if (binding.birthDate.text != null) {
                techData = TechnicianData(
                    binding.firstName.text.toString().trim(),
                    binding.lastName.text.toString().trim(),
                    binding.email.text.toString().trim(),
                    binding.phone.text.toString().trim(),
                    binding.yourCity.selectedItem.toString().trim(),
                    binding.yourGovernorate.selectedItem.toString().trim(),
                    binding.nationalId.text.toString().trim(),
                    binding.professions.selectedItem.toString().trim(),
                    binding.birthDate.text.toString().trim(),
                    binding.createPassword.text.toString().trim()
                )
            } else {
                techData = TechnicianData(
                    binding.firstName.text.toString().trim(),
                    binding.lastName.text.toString().trim(),
                    binding.email.text.toString().trim(),
                    binding.phone.text.toString().trim(),
                    binding.yourCity.selectedItem.toString().trim(),
                    binding.yourGovernorate.selectedItem.toString().trim(),
                    binding.nationalId.text.toString().trim(),
                    binding.professions.selectedItem.toString().trim(),
                    "2000/16/12",
                    binding.createPassword.text.toString().trim()
                )
            }

            Log.d("Test_SignUp", techData.toString())
            if (validate(techData)) {

                viewModel.setTechData(techData)
                viewModel.signUpData.value?.let { it1 -> viewModel.signUp(it1) }
                swipeToLoginFragment(0)

            } else {
                Snackbar.make(view, "Please Chick SignUp Data", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        binding.login.setOnClickListener {
            swipeToLoginFragment(0)
        }

        viewModel.signUpLoadingStatus.observe(viewLifecycleOwner) {
            it.let {

                when (viewModel.signUpLoadingStatus.value) {
                    TechSignUpApiStatus.LOADING -> {
                        loadingProgressBar.visible(true)
                    }
                    TechSignUpApiStatus.DONE -> {
                        loadingProgressBar.visible(false)
                        cleanForm()
                        Toast.makeText(
                            context,
                            viewModel.signUpResponseData.value?.msg.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    TechSignUpApiStatus.ERROR -> {

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
                //loadingProgressBar.visible(false)
            }
        }

        binding.birthDate.setOnClickListener {
            showPopUpUpdateLoc()
            popupWindow = showPopUpUpdateLoc()
            popupWindow?.isOutsideTouchable = true
            popupWindow?.isFocusable = true
            popupWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow?.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
        }
    }


    private fun cleanForm() {
        binding.firstName.text = null
        binding.lastName.text = null
        binding.email.text = null
        binding.phone.text = null
        binding.nationalId.text = null
        binding.birthDate.text = null
        binding.createPassword.text = null
        binding.repeatPassword.text = null
    }

    //https://stackoverflow.com/questions/41649074/how-do-i-set-onclick-validation-for-registration-form-in-android-studio
    private fun validate(tech: TechnicianData): Boolean {
        var valid = true

        if (!stringValidator(tech.firstName)) {
            binding.firstName.error = "Not Valid Name"
            valid = false
        }
        if (!stringValidator(tech.lastName)) {
            binding.lastName.error = "Not Valid Name"
            valid = false
        }
        if (!emailValidator(tech.email)) {
            binding.email.error = "Not Valid E-Mail"
            valid = false
        }
        if (!phoneValidator(tech.phone)) {
            binding.phone.error = "Not Valid Phone"
            valid = false
        }
        if (!nationalIdValidator(tech.natinalId)) {
            binding.nationalId.error = "Not Valid National Id /n Should be 14 Digits"
            valid = false
        }
        if (!birthDateValidator(tech.birthDate)) {
            binding.birthDate.error = "Not Valid BirthDate"
            valid = false
        } else {
            binding.birthDate.error = null
        }
        if (!passwordValidation(tech.password)) {
            binding.createPassword.error = "Not Valid Password"
            valid = false
        }
        if (binding.repeatPassword.text.toString() != binding.createPassword.text.toString()) {
            binding.repeatPassword.error = "Passwords Mismatched"
            valid = false
        }
        return valid
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

    private fun nationalIdValidator(_nationalId: String): Boolean {
        return !(_nationalId.isEmpty() || _nationalId.length != 14)
    }


    //https://stackoverflow.com/questions/59301052/how-to-validate-an-input-string-as-a-valid-date-in-kotlin
    //https://stackoverflow.com/questions/226910/how-to-sanity-check-a-date-in-java/892204#892204
    //https://stackoverflow.com/questions/226910/how-to-sanity-check-a-date-in-java
    @SuppressLint("SimpleDateFormat")
    private fun birthDateValidator(_birthDate: String): Boolean {
        return if (_birthDate.isEmpty()) {
            false
        } else {
            try {
                val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                df.isLenient = false
                df.parse(_birthDate)
                true
            } catch (e: ParseException) {
                false
            }
        }
    }

    private fun passwordValidation(_password: String): Boolean {
        return !(_password.isEmpty() || _password.length < 8)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanForm()
        _binding = null
    }

    //Swipe to login page
    private fun swipeToLoginFragment(item: Int) {
        signUpPagerViewModel.setPagerSelectedItem(item)
        //(Fragment() as TechnicianPagerFragment).setCurrentItem(0)
    }

    private fun showPopUpUpdateLoc(): PopupWindow {
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_enter_date, null)
        _popBinding = PopupEnterDateBinding.bind(view)

        popBinding.btnEnter.setOnClickListener {
            birthDateValidator(popBinding.datePicker.maxDate.toString())
            viewModel.setBirthDate(
                popBinding.datePicker.year,
                popBinding.datePicker.month,
                popBinding.datePicker.dayOfMonth
            )
            binding.birthDate.text = viewModel.birthDate.value
            dismissPopup()
        }

        return PopupWindow(
            popBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun dismissPopup() {
        popupWindow?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            popupWindow = null
        }
    }
}
