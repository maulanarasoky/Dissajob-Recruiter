package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.*
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantAdapter
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.DateUtils
import org.d3ifcool.dissajobrecruiter.vo.Status

class JobDetailsActivity : AppCompatActivity(), View.OnClickListener,
    ApplicantAdapter.LoadApplicantDetailsCallback {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var activityJobDetailsBinding: ActivityJobDetailsBinding

    private lateinit var jobViewModel: JobViewModel

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var applicantAdapter: ApplicantAdapter

    private lateinit var jobData: JobDetailsEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityJobDetailsBinding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(activityJobDetailsBinding.root)

        val extras = intent.extras
        if (extras != null) {
            val factory = ViewModelFactory.getInstance(this)
            jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            val jobId = extras.getString(EXTRA_ID)
            if (jobId != null) {
                showJobDetails(jobId)
            }
        }

        activityJobDetailsBinding.jobDetailsHeader.imgBackBtn.setOnClickListener(this)
        activityJobDetailsBinding.jobDetailsHeader.imgEditJobBtn.setOnClickListener(this)
    }

    private fun showJobDetails(jobId: String) {
        jobViewModel.getJobDetails(jobId).observe(this) { jobDetails ->
            if (jobDetails.data != null) {
                when (jobDetails.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        jobData = jobDetails.data
                        populateData(jobDetails.data)
                        activityJobDetailsBinding.jobDetailsHeader.imgEditJobBtn.isClickable =
                            true
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun populateData(jobDetails: JobDetailsEntity) {
        //Title section
        activityJobDetailsBinding.jobDetailsTitleSection.tvJobTitle.text =
            jobDetails.title.toString()
        val postedDate = DateUtils.getPostedDate(jobDetails.postedDate.toString())
        getApplicantsByJob(jobDetails.id)
        activityJobDetailsBinding.jobDetailsTitleSection.tvJobPostedDateAndApplicants.text =
            resources.getString(
                R.string.job_details_date_format,
                postedDate,
                applicantAdapter.itemCount
            )

        //Description section
        activityJobDetailsBinding.jobDetailsDescriptionSection.tvDescription.text =
            jobDetails.description.toString()

        //Details section
        activityJobDetailsBinding.jobDetailsDetailsSection.tvQualification.text =
            jobDetails.qualification.toString()
        activityJobDetailsBinding.jobDetailsDetailsSection.tvEmployment.text =
            jobDetails.employment.toString()
        activityJobDetailsBinding.jobDetailsDetailsSection.tvIndustry.text =
            jobDetails.industry.toString()
        activityJobDetailsBinding.jobDetailsDetailsSection.tvSalary.text =
            jobDetails.salary.toString()
    }

    private fun getApplicantsByJob(jobId: String) {
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
        applicantAdapter = ApplicantAdapter(this)
        viewModel.getApplicantsByJob(jobId).observe(this) { applicants ->
            if (applicants.data != null) {
                when (applicants.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (applicants.data.isNotEmpty()) {
                            applicantAdapter.submitList(applicants.data)
                            applicantAdapter.notifyDataSetChanged()
                        } else {
                            activityJobDetailsBinding.jobDetailsApplicantsSection.tvNoData.visibility =
                                View.VISIBLE
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBackBtn -> finish()
            R.id.imgEditJobBtn -> {
                val intent = Intent(this, CreateEditJobActivity::class.java)
                intent.putExtra(CreateEditJobActivity.JOB_EXTRA, jobData)
                startActivityForResult(intent, CreateEditJobActivity.REQUEST_UPDATE)
            }
        }
    }

    override fun onLoadApplicantDetailsCallback(
        applicantId: String,
        callback: ApplicantAdapter.LoadApplicantDetailsCallback
    ) {
        applicantViewModel.getApplicantDetails(applicantId).observe(this) { applicantDetails ->
            if (applicantDetails.data != null) {
                callback.onGetApplicantDetails(applicantDetails.data)
            }
        }
    }

    override fun onGetApplicantDetails(applicantDetails: ApplicantDetailsEntity) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreateEditJobActivity.REQUEST_UPDATE) {
            if (resultCode == CreateEditJobActivity.RESULT_UPDATE) {
                val jobId = intent.extras?.getString(EXTRA_ID)
                showJobDetails(jobId.toString())
            }
        }
    }
}