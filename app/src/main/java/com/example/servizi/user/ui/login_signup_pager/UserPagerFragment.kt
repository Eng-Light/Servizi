package com.example.servizi.user.ui.login_signup_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.servizi.databinding.FragmentUserPagerBinding
import com.example.servizi.technician.ui.login_signup_pager.PagerViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/** A simple [Fragment] subclass as the default destination in the navigation.*/


class UserPagerFragment : Fragment() {

    private var _binding: FragmentUserPagerBinding? = null
    private val pagerViewModel: PagerViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserPagerBinding.inflate(inflater, container, false)

        val fMr = parentFragmentManager
        val sectionsPagerAdapter = SectionsPagerAdapter(fMr, lifecycle)
        val viewPager: ViewPager2 = binding.userViewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.userTabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = context?.resources?.getString(sectionsPagerAdapter.titles[position])
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerViewModel.pagerItem.observe(viewLifecycleOwner) {

            binding.userViewPager.currentItem = it

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
