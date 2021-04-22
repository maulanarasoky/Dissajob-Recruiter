package org.d3ifcool.dissajobrecruiter.ui.home.pager

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
import org.d3ifcool.dissajobrecruiter.databinding.FragmentAllApplicantsBinding
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationAdapter
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class AllApplicantsFragment : Fragment(), ApplicationAdapter.LoadApplicantDataCallback {

    private lateinit var fragmentAllApplicantsBinding: FragmentAllApplicantsBinding

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var applicationViewModel: ApplicationViewModel

    private lateinit var applicationAdapter: ApplicationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllApplicantsBinding =
            FragmentAllApplicantsBinding.inflate(layoutInflater, container, false)
        return fragmentAllApplicantsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            showLoading(true)
            val factory = ViewModelFactory.getInstance(requireContext())

            applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]

            applicationViewModel = ViewModelProvider(this, factory)[ApplicationViewModel::class.java]
            applicationAdapter = ApplicationAdapter(this)
            applicationViewModel.getApplications().observe(viewLifecycleOwner) { applicants ->
                if (applicants != null) {
                    when (applicants.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (applicants.data?.isNotEmpty() == true) {
                                applicationAdapter.submitList(applicants.data)
                                applicationAdapter.notifyDataSetChanged()
                            } else {
                                fragmentAllApplicantsBinding.tvNoData.visibility = View.VISIBLE
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

        with(fragmentAllApplicantsBinding.rvApplicant) {
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
            fragmentAllApplicantsBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentAllApplicantsBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onLoadApplicantDetailsCallback(
        applicantId: String,
        callback: ApplicationAdapter.LoadApplicantDataCallback
    ) {
        applicantViewModel.getApplicantDetails(applicantId).observe(viewLifecycleOwner) { applicantDetails ->
            if (applicantDetails != null) {
                callback.onGetApplicantDetails(applicantDetails.data!!)
            }
        }
    }

    override fun onGetApplicantDetails(applicantDetails: ApplicantEntity) {
    }
}