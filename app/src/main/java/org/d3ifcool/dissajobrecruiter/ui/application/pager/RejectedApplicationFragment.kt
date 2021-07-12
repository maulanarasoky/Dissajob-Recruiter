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
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.FragmentRejectedApplicationBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.applicant.LoadApplicantDataCallback
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationAdapter
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationDetailsActivity
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.callback.OnApplicationClickCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.SendApplicationDataCallback
import org.d3ifcool.dissajobrecruiter.ui.job.JobViewModel
import org.d3ifcool.dissajobrecruiter.ui.job.callback.LoadJobDataCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class RejectedApplicationFragment : Fragment(), LoadApplicantDataCallback,
    OnApplicationClickCallback, LoadJobDataCallback, SendApplicationDataCallback {

    private lateinit var fragmentRejectedApplicationBinding: FragmentRejectedApplicationBinding

    private lateinit var jobViewModel: JobViewModel

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var applicationViewModel: ApplicationViewModel

    private lateinit var applicationAdapter: ApplicationAdapter

    private val recruiterId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentRejectedApplicationBinding =
            FragmentRejectedApplicationBinding.inflate(layoutInflater, container, false)
        return fragmentRejectedApplicationBinding.root
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
            applicationAdapter = ApplicationAdapter(this, this, this, this)
            applicationViewModel.getRejectedApplications(recruiterId)
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

        with(fragmentRejectedApplicationBinding.rvApplication) {
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
            fragmentRejectedApplicationBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentRejectedApplicationBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView(state: Boolean) {
        if (state) {
            fragmentRejectedApplicationBinding.rvApplication.visibility = View.VISIBLE
            fragmentRejectedApplicationBinding.tvNoData.visibility = View.GONE
        } else {
            fragmentRejectedApplicationBinding.rvApplication.visibility = View.GONE
            fragmentRejectedApplicationBinding.tvNoData.visibility = View.VISIBLE
        }
    }

    override fun onLoadApplicantDetailsCallback(
        applicantId: String,
        callback: LoadApplicantDataCallback
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
        callback: LoadJobDataCallback
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
        TODO("Not yet implemented")
    }

    override fun onItemClick(applicationId: String, jobId: String, applicantId: String) {
        val intent = Intent(activity, ApplicationDetailsActivity::class.java)
        intent.putExtra(ApplicationDetailsActivity.APPLICATION_ID, applicationId)
        intent.putExtra(ApplicationDetailsActivity.JOB_ID, jobId)
        intent.putExtra(ApplicationDetailsActivity.APPLICANT_ID, applicantId)
        activity?.startActivity(intent)
    }

    override fun sendApplicationAndApplicantData(
        applicationEntity: ApplicationEntity,
        applicantEntity: ApplicantEntity,
        jobDetailsEntity: JobDetailsEntity
    ) {
    }
}