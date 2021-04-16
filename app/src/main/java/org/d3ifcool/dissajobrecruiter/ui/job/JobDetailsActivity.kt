package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.*
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantAdapter
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.job.callback.DeleteJobCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.DateUtils
import org.d3ifcool.dissajobrecruiter.vo.Status

class JobDetailsActivity : AppCompatActivity(),
    ApplicantAdapter.LoadApplicantDetailsCallback, DeleteJobCallback {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var activityJobDetailsBinding: ActivityJobDetailsBinding

    private lateinit var jobViewModel: JobViewModel

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var applicantAdapter: ApplicantAdapter

    private lateinit var jobData: JobDetailsEntity

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityJobDetailsBinding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(activityJobDetailsBinding.root)

        activityJobDetailsBinding.toolbar.title = resources.getString(R.string.job_details)
        setSupportActionBar(activityJobDetailsBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            val factory = ViewModelFactory.getInstance(this)
            jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            val jobId = extras.getString(EXTRA_ID)
            if (jobId != null) {
                showJobDetails(jobId)
            }
        }
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
        applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
        applicantAdapter = ApplicantAdapter(this)
        applicantViewModel.getApplicantsByJob(jobId).observe(this) { applicants ->
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.job_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.editJob -> {
                val intent = Intent(this, CreateEditCreateJobActivity::class.java)
                intent.putExtra(CreateEditCreateJobActivity.JOB_EXTRA, jobData)
                startActivityForResult(intent, CreateEditCreateJobActivity.REQUEST_UPDATE)
                true
            }
            R.id.deleteJob -> {
                dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                dialog.titleText = resources.getString(R.string.confirm_alert_delete_job)
                dialog.setCancelable(true)
                dialog.cancelText = resources.getString(R.string.txt_cancel)
                dialog.confirmText = resources.getString(R.string.delete_job)
                dialog.setConfirmClickListener {
                    dialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
                    dialog.titleText = resources.getString(R.string.loading)
                    dialog.showCancelButton(false)
                    dialog.setCancelable(false)
                    jobViewModel.deleteJob(jobData.id, this)
                }
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        if (requestCode == CreateEditCreateJobActivity.REQUEST_UPDATE) {
            if (resultCode == CreateEditCreateJobActivity.RESULT_UPDATE) {
                val jobId = intent.extras?.getString(EXTRA_ID)
                showJobDetails(jobId.toString())
            }
        }
    }

    override fun onDeleteSuccess() {
        dialog.dismissWithAnimation()
        Toast.makeText(
            this,
            resources.getString(R.string.success_alert_delete_job),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    override fun onDeleteFailure(messageId: Int) {
        Toast.makeText(
            this,
            resources.getString(messageId),
            Toast.LENGTH_SHORT
        ).show()
    }
}