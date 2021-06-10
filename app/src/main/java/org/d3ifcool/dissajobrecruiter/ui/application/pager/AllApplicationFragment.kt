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
import org.d3ifcool.dissajobrecruiter.databinding.FragmentAllApplicationBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationAdapter
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationDetailsActivity
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.callback.OnApplicationClickCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class AllApplicationFragment : Fragment(), ApplicationAdapter.LoadApplicantDataCallback,
    OnApplicationClickCallback {

    private lateinit var fragmentAllApplicationBinding: FragmentAllApplicationBinding

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var applicationViewModel: ApplicationViewModel

    private lateinit var applicationAdapter: ApplicationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllApplicationBinding =
            FragmentAllApplicationBinding.inflate(layoutInflater, container, false)
        return fragmentAllApplicationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            showLoading(true)
            val factory = ViewModelFactory.getInstance(requireContext())

            applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]

            applicationViewModel =
                ViewModelProvider(this, factory)[ApplicationViewModel::class.java]
            applicationAdapter = ApplicationAdapter(this, this)
            applicationViewModel.getApplications().observe(viewLifecycleOwner) { applications ->
                if (applications != null) {
                    when (applications.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (applications.data?.isNotEmpty() == true) {
                                applicationAdapter.submitList(applications.data)
                                applicationAdapter.notifyDataSetChanged()
                            } else {
                                fragmentAllApplicationBinding.tvNoData.visibility = View.VISIBLE
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

        with(fragmentAllApplicationBinding.rvApplication) {
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
            fragmentAllApplicationBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentAllApplicationBinding.progressBar.visibility = View.GONE
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

    override fun onItemClick(applicationId: String, jobId: String, applicantId: String) {
        val intent = Intent(activity, ApplicationDetailsActivity::class.java)
        intent.putExtra(ApplicationDetailsActivity.APPLICATION_ID, applicationId)
        intent.putExtra(ApplicationDetailsActivity.JOB_ID, jobId)
        intent.putExtra(ApplicationDetailsActivity.APPLICANT_ID, applicantId)
        activity?.startActivity(intent)
    }
}