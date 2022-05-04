package com.example.servizi.user.ui.book_sheets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.servizi.R
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentUserBookBottomSheetBinding
import com.example.servizi.databinding.PopupEnterDateBinding
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.model.BookAppRequestData
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.network.RemoteDataSource
import com.example.servizi.user.network.UserApiService
import com.example.servizi.user.ui.home.UserSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BookBottomSheetFragment : BottomSheetDialogFragment() {
    private var popupWindow: PopupWindow? = null
    private var _popBinding: PopupEnterDateBinding? = null
    private val popBinding get() = _popBinding!!
    private var newApp: BookAppRequestData? = null
    private lateinit var userPreferences: UserPreferences
    private val remoteDataSource = RemoteDataSource()
    private val userSharedModel: UserSharedViewModel by activityViewModels()
    val binding: FragmentUserBookBottomSheetBinding by lazy {
        FragmentUserBookBottomSheetBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: BookBottomSheetViewModel
    fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return UserRepository(api)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())
        // binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory)[BookBottomSheetViewModel::class.java]
        lifecycleScope.launch { userPreferences.accessToken.first() }
        //  binding = getFragmentBinding(inflater, container)
        binding.sharedViewModel = userSharedModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tDate.setOnClickListener {
            showUpdate()
            popupWindow = showUpdate()
            popupWindow?.isOutsideTouchable = true
            popupWindow?.isFocusable = true
            popupWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow?.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
        }
        binding.bottomCancel.setOnClickListener {
            //   findNavController().navigate(dismiss())
            dismiss()
        }
        binding.buttonBook.setOnClickListener {

            newApp = if (binding.tDate.text != null) {

                BookAppRequestData(
                    binding.tDate.text.toString().trim(),
                    binding.address.text.toString().trim(),
                    binding.sTime.selectedItem.toString().trim(),
                    binding.description.text.toString().trim(),
                    userSharedModel.techId.value!!
                )
            } else {
                BookAppRequestData(
                    "2022/16/12",
                    binding.address.text.toString().trim(),
                    binding.sTime.selectedItem.toString().trim(),
                    binding.description.text.toString().trim(),
                    userSharedModel.techId.value!!
                )
            }

            if (validate(newApp!!.date, newApp!!.description, newApp!!.address)) {
                viewModel.bookApp(newApp!!)
            } else {
                Snackbar.make(view, "Please Check Booking Data", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.bookingResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    binding.loading1.visible(false)
                    Toast.makeText(requireContext(), it.data.msg, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_bookBottomSheetFragment_to_MyOrdersFragment)
                    viewModel._bookingResponse.value = null
                }
                is Result.Loading -> {
                    binding.loading1.visible(true)
                }
                is Result.Error -> {
                    handleApiError(it) {
                        viewModel.bookApp(newApp!!)
                    }
                    binding.loading1.visible(false)
                }
            }
        }
    }

    private fun dismissPopup() {
        popupWindow?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            popupWindow = null
        }
    }

    private fun showUpdate(): PopupWindow {
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_enter_date, null)
        _popBinding = PopupEnterDateBinding.bind(view)

        popBinding.btnEnter.setOnClickListener {
            viewModel.setDate(
                popBinding.datePicker.month,
                popBinding.datePicker.dayOfMonth
            )
            binding.tDate.text = viewModel.date.value
            dismissPopup()
        }

        return PopupWindow(
            popBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun validate(date: String, description: String, address: String): Boolean {
        var valid = true

        if (!stringValidator(description)) {
            binding.description.error = "Description must be more than 10 Char"
            valid = false
        }

        if (!addressValidator(address)) {
            binding.address.error = "Address must be more than 3 Char"
            valid = false
        }

        if (!dateValidator(date)) {
            binding.tDate.error = "Not Valid Date"
            valid = false
        } else {
            binding.tDate.error = null
        }

        return valid
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateValidator(_date: String): Boolean {
        return if (_date.isEmpty()) {
            false
        } else {
            try {
                val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                df.isLenient = false
                df.parse(_date)
                true
            } catch (e: ParseException) {
                false
            }
        }
    }

    private fun stringValidator(_name: String): Boolean {
        return !(_name.isEmpty() || _name.length < 11)
    }

    private fun addressValidator(_name: String): Boolean {
        return !(_name.isEmpty() || _name.length < 3)
    }

}