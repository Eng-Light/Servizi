package com.example.servizi.technician.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.databinding.FragmentTechnicianSignUpBinding
import com.example.servizi.technician.model.TechnicianData

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
                binding.age.text.toString(),
                binding.createPassword.text.toString()
            )
            Log.d("Test_SignUp",techData.toString())
            viewModel.setTechData(techData)
            viewModel.signUpData.value?.let { it1 -> viewModel.signUp(it1) }
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
