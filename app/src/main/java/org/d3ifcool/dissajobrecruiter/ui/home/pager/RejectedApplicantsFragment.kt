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
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.FragmentRejectedApplicantsBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantAdapter
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class RejectedApplicantsFragment : Fragment(), ApplicantAdapter.LoadApplicantDetailsCallback {

    private lateinit var fragmentRejectedApplicantsBinding: FragmentRejectedApplicantsBinding

    private lateinit var viewModel: ApplicantViewModel

    private lateinit var applicantAdapter: ApplicantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentRejectedApplicantsBinding =
            FragmentRejectedApplicantsBinding.inflate(layoutInflater, container, false)
        return fragmentRejectedApplicantsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            showLoading(true)
            val factory = ViewModelFactory.getInstance(requireContext())
            val viewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
            applicantAdapter = ApplicantAdapter(this)
            viewModel.getRejectedApplicants().observe(viewLifecycleOwner) { applicants ->
                if (applicants != null) {
                    when (applicants.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (applicants.data?.isNotEmpty() == true) {
                                applicantAdapter.submitList(applicants.data)
                                applicantAdapter.notifyDataSetChanged()
                            } else {
                                fragmentRejectedApplicantsBinding.tvNoData.visibility = View.VISIBLE
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

        with(fragmentRejectedApplicantsBinding.rvApplicant) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = applicantAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fragmentRejectedApplicantsBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentRejectedApplicantsBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onLoadApplicantDetailsCallback(
        applicantId: String,
        callback: ApplicantAdapter.LoadApplicantDetailsCallback
    ) {
        viewModel.getApplicantDetails(applicantId).observe(viewLifecycleOwner) { applicantDetails ->
            if (applicantDetails != null) {
                callback.onGetApplicantDetails(applicantDetails.data!!)
            }
        }
    }

    override fun onGetApplicantDetails(applicantDetails: ApplicantDetailsEntity) {
    }
}