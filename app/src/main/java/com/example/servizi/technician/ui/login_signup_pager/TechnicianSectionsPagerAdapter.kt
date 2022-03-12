package com.example.servizi.technician.ui.login_signup_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.servizi.R
import com.example.servizi.signup_login_pager.PlaceholderFragment
import com.example.servizi.technician.ui.login.LoginFragment
import com.example.servizi.technician.ui.signup.SignUpFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class TechnicianSectionsPagerAdapter(private val context: TechnicianPagerFragment, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when(position){
            0 -> LoginFragment()
            1 -> SignUpFragment()
            else -> {PlaceholderFragment.newInstance(position+1)}
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}