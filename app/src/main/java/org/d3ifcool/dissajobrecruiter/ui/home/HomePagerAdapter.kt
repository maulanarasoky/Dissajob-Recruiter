package org.d3ifcool.dissajobrecruiter.ui.home

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.ui.application.pager.AcceptedApplicationFragment
import org.d3ifcool.dissajobrecruiter.ui.application.pager.AllApplicationFragment
import org.d3ifcool.dissajobrecruiter.ui.application.pager.MarkedApplicationFragment
import org.d3ifcool.dissajobrecruiter.ui.application.pager.RejectedApplicationFragment

class HomePagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.txt_all,
            R.string.txt_accepted,
            R.string.txt_marked,
            R.string.txt_rejected
        )
    }

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> AllApplicationFragment()
            1 -> AcceptedApplicationFragment()
            2 -> MarkedApplicationFragment()
            3 -> RejectedApplicationFragment()
            else -> Fragment()
        }

    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(
        TAB_TITLES[position]
    )

    override fun getCount(): Int = 4
}