package com.example.servizi.technician.ui.signup

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.databinding.FragmentTechnicianSignUpBinding
import com.example.servizi.technician.model.signup.TechnicianData
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
    private var _binding: FragmentTechnicianSignUpBinding? = null

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

        binding.btnRegister.setOnClickListener {

            val techData = TechnicianData(
                binding.firstName.text.toString(),
                binding.lastName.text.toString(),
                binding.email.text.toString(),
                binding.phone.text.toString(),
                binding.city.selectedItem.toString(),
                binding.governorate.selectedItem.toString(),
                binding.nationalId.text.toString(),
                binding.profession.text.toString(),
                binding.birthDate.text.toString(),
                binding.createPassword.text.toString()
            )

            Log.d("Test_SignUp", techData.toString())

            if (validate(techData)) {

                viewModel.setTechData(techData)
                viewModel.signUpData.value?.let { it1 -> viewModel.signUp(it1) }

            } else {
                Snackbar.make(view, "Please Chick SignUp Data", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.signUpLoadingStatus.observe(viewLifecycleOwner) {
            it.let {

                if (viewModel.signUpLoadingStatus.value == TechSignUpApiStatus.DONE) {

                    cleanForm()
                    Toast.makeText(
                        context,
                        viewModel.signUpResponseData.value?.msg.toString(),
                        Toast.LENGTH_LONG
                    ).show()

                } else if (viewModel.signUpLoadingStatus.value == TechSignUpApiStatus.ERROR) {

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

    private fun cleanForm() {
        binding.firstName.text = null
        binding.lastName.text = null
        binding.email.text = null
        binding.phone.text = null
        binding.nationalId.text = null
        binding.profession.text = null
        binding.birthDate.text = null
        binding.createPassword.text = null
        binding.repeatPassword.text = null
    }

    /**companion object {

     * The fragment argument representing the section number for this
     * fragment.

    private const val ARG_SECTION_NUMBER = "section_number"


     * Returns a new instance of this fragment for the given section
     * number.

    @JvmStatic
    fun newInstance(sectionNumber: Int): PlaceholderFragment {
    return PlaceholderFragment().apply {
    arguments = Bundle().apply {
    putInt(ARG_SECTION_NUMBER, sectionNumber)
    }
    }
    }
    }**/


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
            binding.nationalId.error = "Not Valid National Id"
            valid = false
        }
        if (!stringValidator(tech.profession)) {
            binding.profession.error = "Not Valid Profession"
            valid = false
        }
        if (!birthDateValidator(tech.birthDate)) {
            binding.birthDate.error = "Not Valid BirthDate"
            valid = false
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
        return !(_nationalId.isEmpty() || _nationalId.length < 10 || _nationalId.length > 13)
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
}
