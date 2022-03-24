package com.example.servizi.user.ui.login_signup_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.servizi.R
import com.example.servizi.signup_login_pager.PlaceholderFragment
import com.example.servizi.technician.ui.login.LoginFragment

import  com.example.servizi.user.ui.signup.UserSignupFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.*/


class SectionsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    val titles = TAB_TITLES

    override fun createFragment(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when (position) {
            0 -> LoginFragment()
            1 -> UserSignupFragment()
            else -> PlaceholderFragment.newInstance(position + 1)
        }
    }

    override fun getItemCount(): Int {
        // Show 2 total pages.
        return 2
    }

}