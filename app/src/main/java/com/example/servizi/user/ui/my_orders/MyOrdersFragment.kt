package com.example.servizi.user.ui.my_orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.servizi.databinding.FragmentUserMyOrdersBinding
import com.example.servizi.databinding.FragmentUserReviewsBinding
import com.example.servizi.user.ui.reviews.ReviewsAdapter

class MyOrdersFragment : Fragment() {

    private var _binding: FragmentUserMyOrdersBinding? = null
    private lateinit var viewModel: MyOrdersViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserMyOrdersBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MyOrdersViewModel::class.java]
    }

}