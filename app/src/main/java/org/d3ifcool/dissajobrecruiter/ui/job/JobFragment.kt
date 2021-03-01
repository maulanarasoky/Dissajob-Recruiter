package org.d3ifcool.dissajobrecruiter.ui.job

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.FragmentJobBinding
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class JobFragment : Fragment() {
    private lateinit var fragmentJobBinding: FragmentJobBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentJobBinding = FragmentJobBinding.inflate(layoutInflater, container, false)
        return fragmentJobBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            showLoading(true)
            val factory = ViewModelFactory.getInstance(requireContext())
            val viewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            val jobAdapter = JobAdapter()
            viewModel.getJobs().observe(viewLifecycleOwner) { jobs ->
                if (jobs != null) {
                    when (jobs.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            jobAdapter.submitList(jobs.data)
                            jobAdapter.notifyDataSetChanged()
                        }
                        Status.ERROR -> {
                            showLoading(false)
                            Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
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
}