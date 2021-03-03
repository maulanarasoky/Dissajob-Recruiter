package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.FragmentJobBinding
import org.d3ifcool.dissajobrecruiter.databinding.JobFragmentHeaderBinding
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class JobFragment : Fragment(), View.OnClickListener {
    private lateinit var fragmentJobBinding: FragmentJobBinding
    private lateinit var headerJobFragmentBinding: JobFragmentHeaderBinding

    private lateinit var jobAdapter: JobAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentJobBinding = FragmentJobBinding.inflate(layoutInflater, container, false)
        headerJobFragmentBinding =
            JobFragmentHeaderBinding.inflate(layoutInflater, container, true)
        return fragmentJobBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            headerJobFragmentBinding.addJob.setOnClickListener(this)

            showLoading(true)
            val factory = ViewModelFactory.getInstance(requireContext())
            val viewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            jobAdapter = JobAdapter()
            viewModel.getJobs().observe(viewLifecycleOwner) { jobs ->
                if (jobs.isNotEmpty()) {
                    fragmentJobBinding.tvNoData.visibility = View.GONE
                    jobAdapter.setData(jobs)
                    jobAdapter.notifyDataSetChanged()
                }else {
                    fragmentJobBinding.tvNoData.visibility = View.VISIBLE
                }
                showLoading(false)
            }
        }

        with(fragmentJobBinding.rvJob) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = jobAdapter
        }

//        val currentFragment = fragmentManager?.findFragmentByTag(JobFragment::class.java.simpleName)
//        swipeRefreshLayout.setOnRefreshListener {
//            Handler().postDelayed({
//                if (currentFragment != null && currentFragment.isVisible) {
//                    swipeRefreshLayout.isRefreshing = false
//                    showJobs()
//                }
//            }, 2000)
//        }

//        addJob.setOnClickListener {
//            startActivity<JobPostActivity>()
//        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fragmentJobBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentJobBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.addJob) {
            startActivity(Intent(activity, JobPostActivity::class.java))
        }
    }
}