package com.example.servizi.user.ui.reviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.example.servizi.application.BaseFragment
import com.example.servizi.application.ViewModelFactory
import com.example.servizi.databinding.FragmentUserReviewsBinding
import com.example.servizi.databinding.PopupBookReviewsBinding
import com.example.servizi.databinding.PopupEnterDateBinding
import com.example.servizi.technician.model.login.data.Result
import com.example.servizi.technician.model.login.data.UserPreferences
import com.example.servizi.technician.ui.login.handleApiError
import com.example.servizi.technician.ui.login.visible
import com.example.servizi.user.model.BookAppRequestData
import com.example.servizi.user.model.UserRepository
import com.example.servizi.user.network.UserApiService
import com.example.servizi.user.ui.home.UserSharedViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReviewsFragment :
    BaseFragment<ReviewsViewModel, FragmentUserReviewsBinding, UserRepository>() {
    private val userSharedModel: UserSharedViewModel by activityViewModels()
    private var popupWindow: PopupWindow? = null
    private var _popBinding: PopupBookReviewsBinding? = null
    private val popBinding get() = _popBinding!!
    private var _pop: PopupEnterDateBinding? = null
    private val pop get() = _pop!!
    private var newApp: BookAppRequestData? = null

    override fun getViewModel() = ReviewsViewModel::class.java

    override fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.accessToken.first().toString() }
        val api = remoteDataSource.buildApi(UserApiService::class.java, token)
        return UserRepository(api)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserReviewsBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val loadingProgressBar = binding.loading1
        userPreferences = UserPreferences(requireContext())

        binding = getFragmentBinding(inflater, container)

        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory)[getViewModel()]

        lifecycleScope.launch { userPreferences.accessToken.first() }

        binding.bttBook.setOnClickListener() {
            Log.d("ReviewsFragment", "Clicked")
            showPopBook()
            popupWindow = showPopBook()
            popupWindow?.isOutsideTouchable = true
            popupWindow?.isFocusable = true
            popupWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow?.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ReviewsAdapter()
        adapter.submitList(
            arrayListOf(
                "Nour", "Mayar", "Manar", "Saif", "Nada", "Talaa", "Khaled", "Abd Allah",
                "Jasmin",
                "Talaa",
                "Khaled",
                "Abd Allah",
                "Jasmin"
            )
        )
        binding.rvShowReview.adapter = adapter

        viewModel.bookingResponse.observe(viewLifecycleOwner) {
            binding.loading1.visible(true)
            when (it) {
                is Result.Success -> {
                    binding.loading1.visible(false)
                    Toast.makeText(requireContext(), it.data.msg, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.navigation_my_orders)
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

    private fun showPopBook(): PopupWindow {
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_book_reviews, null)
        _popBinding = PopupBookReviewsBinding.bind(view)

        popBinding.buttonBook.setOnClickListener() {
            newApp = if (popBinding.tDate.text != null) {
                BookAppRequestData(
                    popBinding.tDate.text.toString().trim(),
                    popBinding.address.text.toString().trim(),
                    popBinding.sTime.selectedItem.toString().trim(),
                    popBinding.description.text.toString().trim(),
                    userSharedModel.techId.value!!
                )
            } else {
                BookAppRequestData(
                    "2022/16/12",
                    popBinding.address.text.toString().trim(),
                    popBinding.sTime.selectedItem.toString().trim(),
                    popBinding.description.text.toString().trim(),
                    userSharedModel.techId.value!!
                )
            }

            if (validate(newApp!!.date, newApp!!.description, newApp!!.address)) {
                viewModel.bookApp(newApp!!)
            } else {
                Snackbar.make(view, "Please Check Booking Data", Snackbar.LENGTH_SHORT)
                    .show()
            }
            /*viewModel.setDate(
                pop.datePicker.month,
                pop.datePicker.dayOfMonth
            )
            popBinding.tDate.text = viewModel.date.value
            dismissPopup()*/

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

    private fun validate(date: String, description: String, address: String): Boolean {
        var valid = true

        if (!stringValidator(description)) {
            popBinding.description.error = "Description must be more than 10 Char"
            valid = false
        }

        if (!addressValidator(address)) {
            popBinding.address.error = "Address must be more than 3 Char"
            valid = false
        }

        if (!dateValidator(date)) {
            popBinding.tDate.error = "Not Valid Date"
            valid = false
        } else {
            popBinding.tDate.error = null
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
