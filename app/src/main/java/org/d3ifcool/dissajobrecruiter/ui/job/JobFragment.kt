package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.FragmentJobBinding
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class JobFragment : Fragment(), View.OnClickListener, JobAdapter.ItemClickListener {
    private lateinit var fragmentJobBinding: FragmentJobBinding

    private lateinit var jobAdapter: JobAdapter

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
            jobAdapter = JobAdapter(this)
            viewModel.getJobs().observe(viewLifecycleOwner) { jobs ->
                if (jobs != null) {
                    when (jobs.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (jobs.data?.isNotEmpty() == true) {
                                jobAdapter.submitList(jobs.data)
                                jobAdapter.notifyDataSetChanged()
                            } else {
                                fragmentJobBinding.tvNoData.visibility = View.VISIBLE
                            }
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
            adapter = jobAdapter
        }

        fragmentJobBinding.fabAddJob.setOnClickListener(this)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fragmentJobBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentJobBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fabAddJob -> {
                startActivity(Intent(activity, CreateEditJobActivity::class.java))
            }
        }
    }

    override fun onItemClicked(jobId: String) {
        val intent = Intent(activity, JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
        startActivity(intent)
    }
}