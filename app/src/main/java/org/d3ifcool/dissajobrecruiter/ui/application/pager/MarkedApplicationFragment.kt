package org.d3ifcool.dissajobrecruiter.ui.application.pager

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
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.FragmentMarkedApplicationBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationAdapter
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationDetailsActivity
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.callback.OnApplicationClickCallback
import org.d3ifcool.dissajobrecruiter.ui.job.JobViewModel
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.AuthHelper
import org.d3ifcool.dissajobrecruiter.vo.Status

class MarkedApplicationFragment : Fragment(), ApplicationAdapter.LoadApplicantDataCallback,
    OnApplicationClickCallback, ApplicationAdapter.LoadJobDataCallback {

    private lateinit var fragmentMarkedApplicationBinding: FragmentMarkedApplicationBinding

    private lateinit var jobViewModel: JobViewModel

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var applicationViewModel: ApplicationViewModel

    private lateinit var applicationAdapter: ApplicationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMarkedApplicationBinding =
            FragmentMarkedApplicationBinding.inflate(layoutInflater, container, false)
        return fragmentMarkedApplicationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            showLoading(true)
            val factory = ViewModelFactory.getInstance(requireContext())

            jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
            applicationViewModel =
                ViewModelProvider(this, factory)[ApplicationViewModel::class.java]
            applicationAdapter = ApplicationAdapter(this, this, this)
            applicationViewModel.getMarkedApplications(AuthHelper.currentUser?.uid.toString())
                .observe(viewLifecycleOwner) { applications ->
                    if (applications != null) {
                        when (applications.status) {
                            Status.LOADING -> showLoading(true)
                            Status.SUCCESS -> {
                                showLoading(false)
                                if (applications.data?.isNotEmpty() == true) {
                                    applicationAdapter.submitList(applications.data)
                                    applicationAdapter.notifyDataSetChanged()
                                    showRecyclerView(true)
                                } else {
                                    showRecyclerView(false)
                                }
                            }
                            Status.ERROR -> {
                                showLoading(false)
                                showRecyclerView(false)
                                Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }

        with(fragmentMarkedApplicationBinding.rvApplication) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = applicationAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fragmentMarkedApplicationBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentMarkedApplicationBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView(state: Boolean) {
        if (state) {
            fragmentMarkedApplicationBinding.rvApplication.visibility = View.VISIBLE
            fragmentMarkedApplicationBinding.tvNoData.visibility = View.GONE
        } else {
            fragmentMarkedApplicationBinding.rvApplication.visibility = View.GONE
            fragmentMarkedApplicationBinding.tvNoData.visibility = View.VISIBLE
        }
    }

    override fun onLoadApplicantDetailsCallback(
        applicantId: String,
        callback: ApplicationAdapter.LoadApplicantDataCallback
    ) {
        applicantViewModel.getApplicantDetails(applicantId)
            .observe(viewLifecycleOwner) { applicantDetails ->
                if (applicantDetails != null) {
                    if (applicantDetails.data != null) {
                        callback.onGetApplicantDetails(applicantDetails.data)
                    }
                }
            }
    }

    override fun onGetApplicantDetails(applicantDetails: ApplicantEntity) {
    }

    override fun onLoadJobDetailsCallback(
        jobId: String,
        callback: ApplicationAdapter.LoadJobDataCallback
    ) {
        jobViewModel.getJobDetails(jobId).observe(this) { jobDetails ->
            if (jobDetails != null) {
                if (jobDetails.data != null) {
                    callback.onGetJobDetails(jobDetails.data)
                }
            }
        }
    }

    override fun onGetJobDetails(jobDetails: JobDetailsEntity) {
    }

    override fun onItemClick(applicationId: String, jobId: String, applicantId: String) {
        val intent = Intent(activity, ApplicationDetailsActivity::class.java)
        intent.putExtra(ApplicationDetailsActivity.APPLICATION_ID, applicationId)
        intent.putExtra(ApplicationDetailsActivity.JOB_ID, jobId)
        intent.putExtra(ApplicationDetailsActivity.APPLICANT_ID, applicantId)
        activity?.startActivity(intent)
    }
}