package org.d3ifcool.dissajobrecruiter.ui.home

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.ui.home.pager.AcceptedApplicantsFragment
import org.d3ifcool.dissajobrecruiter.ui.home.pager.AllApplicantsFragment
import org.d3ifcool.dissajobrecruiter.ui.home.pager.MarkedApplicantsFragment
import org.d3ifcool.dissajobrecruiter.ui.home.pager.RejectedApplicantsFragment

class HomePagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.all_applicants, R.string.accepted_applicants, R.string.marked_applicants, R.string.rejected_applicants)
    }

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> AllApplicantsFragment()
            1 -> AcceptedApplicantsFragment()
            2 -> MarkedApplicantsFragment()
            3 -> RejectedApplicantsFragment()
            else -> Fragment()
        }

    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(
        TAB_TITLES[position]
    )

    override fun getCount(): Int = 2
}