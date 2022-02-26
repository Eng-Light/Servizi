package com.example.servizi.technician.ui.login_signup_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.servizi.databinding.FragmentTechnicianPagerBinding
import com.google.android.material.tabs.TabLayout

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TechnicianPagerFragment : Fragment() {

    private var _binding: FragmentTechnicianPagerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTechnicianPagerBinding.inflate(inflater, container, false)

        val fMr = parentFragmentManager
        val sectionsPagerAdapter = TechnicianSectionsPagerAdapter(this, fMr)
        val viewPager: ViewPager = binding.technicianViewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.technicianTabs
        tabs.setupWithViewPager(viewPager)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}