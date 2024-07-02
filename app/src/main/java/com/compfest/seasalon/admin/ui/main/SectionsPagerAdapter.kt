package com.compfest.seasalon.admin.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.compfest.seasalon.R
import com.compfest.seasalon.admin.SectionOneFragment
import com.compfest.seasalon.admin.SectionThreeFragment
import com.compfest.seasalon.admin.SectionTwoFragment

private val TAB_TITLES = arrayOf(
    R.string.list_reservation_users,
    R.string.list_history,
    R.string.settings
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SectionOneFragment()
            1 -> SectionTwoFragment()
            else -> SectionThreeFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}