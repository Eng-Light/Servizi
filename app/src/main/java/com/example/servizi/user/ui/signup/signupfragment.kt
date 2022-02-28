package com.example.servizi.user.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.servizi.databinding.FragmentUserSignUpBinding
import com.example.servizi.technician.ui.signup.SignUpViewModel

class signupfragment : Fragment() {
    private lateinit var viewModel: SignUpViewModel
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

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
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

