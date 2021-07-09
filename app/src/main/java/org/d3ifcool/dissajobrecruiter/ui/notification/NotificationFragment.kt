package org.d3ifcool.dissajobrecruiter.ui.notification

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
import org.d3ifcool.dissajobrecruiter.databinding.FragmentNotificationBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.applicant.LoadApplicantDataCallback
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationDetailsActivity
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class NotificationFragment : Fragment(), LoadApplicantDataCallback,
    OnNotificationClickCallback {

    private lateinit var fragmentNotificationBinding: FragmentNotificationBinding

    private lateinit var notificationViewModel: NotificationViewModel

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var notificationAdapter: NotificationAdapter

    private val recruiterId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentNotificationBinding =
            FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return fragmentNotificationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            showLoading(true)
            val factory = ViewModelFactory.getInstance(requireContext())
            applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
            notificationViewModel =
                ViewModelProvider(this, factory)[NotificationViewModel::class.java]
            notificationAdapter = NotificationAdapter(this, this)
            notificationViewModel.getNotifications(recruiterId)
                .observe(viewLifecycleOwner) { applications ->
                    if (applications != null) {
                        when (applications.status) {
                            Status.LOADING -> showLoading(true)
                            Status.SUCCESS -> {
                                showLoading(false)
                                if (applications.data?.isNotEmpty() == true) {
                                    notificationAdapter.submitList(applications.data)
                                    notificationAdapter.notifyDataSetChanged()
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

        with(fragmentNotificationBinding.rvNotification) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = notificationAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fragmentNotificationBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentNotificationBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView(state: Boolean) {
        if (state) {
            fragmentNotificationBinding.rvNotification.visibility = View.VISIBLE
            fragmentNotificationBinding.tvNoData.visibility = View.GONE
        } else {
            fragmentNotificationBinding.rvNotification.visibility = View.GONE
            fragmentNotificationBinding.tvNoData.visibility = View.VISIBLE
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

    override fun onItemClick(applicationId: String, jobId: String, applicantId: String) {
        val intent = Intent(activity, ApplicationDetailsActivity::class.java)
        intent.putExtra(ApplicationDetailsActivity.APPLICATION_ID, applicationId)
        intent.putExtra(ApplicationDetailsActivity.JOB_ID, jobId)
        intent.putExtra(ApplicationDetailsActivity.APPLICANT_ID, applicantId)
        activity?.startActivity(intent)
    }
}