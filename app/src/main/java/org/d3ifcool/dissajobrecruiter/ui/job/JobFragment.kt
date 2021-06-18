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
import org.d3ifcool.dissajobrecruiter.ui.profile.CheckRecruiterDataCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.RecruiterViewModel
import org.d3ifcool.dissajobrecruiter.ui.settings.ChangePhoneNumberActivity
import org.d3ifcool.dissajobrecruiter.ui.settings.ChangeProfileActivity
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.AuthHelper
import org.d3ifcool.dissajobrecruiter.vo.Status

class JobFragment : Fragment(), View.OnClickListener, JobAdapter.ItemClickListener,
    CheckRecruiterDataCallback {
    private lateinit var fragmentJobBinding: FragmentJobBinding

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var jobAdapter: JobAdapter

    private var isBtnClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentJobBinding = FragmentJobBinding.inflate(layoutInflater, container, false)
        return fragmentJobBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            showLoading(true)
            val factory = ViewModelFactory.getInstance(requireContext())
            recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
            val jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            jobAdapter = JobAdapter(this)
            jobViewModel.getJobs(AuthHelper.currentUser?.uid.toString())
                .observe(viewLifecycleOwner) { jobs ->
                    if (jobs.data != null) {
                        when (jobs.status) {
                            Status.LOADING -> showLoading(true)
                            Status.SUCCESS -> {
                                showLoading(false)
                                if (jobs.data.isNotEmpty()) {
                                    jobAdapter.submitList(jobs.data)
                                    jobAdapter.notifyDataSetChanged()
                                    fragmentJobBinding.tvNoData.visibility = View.GONE
                                } else {
                                    fragmentJobBinding.tvNoData.visibility = View.VISIBLE
                                }
                            }
                            Status.ERROR -> {
                                showLoading(false)
                                showToast(R.string.txt_error_occurred)
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

    private fun showToast(messageId: Int) =
        Toast.makeText(requireContext(), resources.getString(messageId), Toast.LENGTH_LONG).show()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabAddJob -> {
                isBtnClicked = true
                recruiterViewModel.checkRecruiterData(AuthHelper.currentUser?.uid.toString(), this)
            }
        }
    }

    override fun onItemClicked(jobId: String) {
        val intent = Intent(activity, JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
        startActivity(intent)
    }

    override fun allDataAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            startActivity(Intent(activity, CreateEditJobActivity::class.java))
        }
    }

    override fun profileDataNotAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            showToast(R.string.txt_fill_all_data_alert)
            startActivity(Intent(activity, ChangeProfileActivity::class.java))
        }
    }

    override fun phoneNumberNotAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            showToast(R.string.txt_fill_all_data_alert)
            startActivity(Intent(activity, ChangePhoneNumberActivity::class.java))
        }
    }
}