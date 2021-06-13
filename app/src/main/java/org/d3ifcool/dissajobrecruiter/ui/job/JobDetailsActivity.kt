package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.ActivityJobDetailsBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationAdapter
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationDetailsActivity
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.callback.OnApplicationClickCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.DeleteJobCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.DateUtils
import org.d3ifcool.dissajobrecruiter.vo.Status

class JobDetailsActivity : AppCompatActivity(),
    ApplicationAdapter.LoadApplicantDataCallback, DeleteJobCallback, OnApplicationClickCallback,
    ApplicationAdapter.LoadJobDataCallback {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var activityJobDetailsBinding: ActivityJobDetailsBinding

    private lateinit var jobViewModel: JobViewModel

    private lateinit var applicationViewModel: ApplicationViewModel

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var applicationAdapter: ApplicationAdapter

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
                getApplicationsByJob(jobId)
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
            jobDetails.title

        activityJobDetailsBinding.jobDetailsTitleSection.tvJobAddress.text =
            jobDetails.address

        activityJobDetailsBinding.jobDetailsTitleSection.tvJobType.text = resources.getString(
            R.string.txt_job_type_value,
            jobDetails.employment,
            jobDetails.type
        )

        //Description section
        activityJobDetailsBinding.jobDetailsDescriptionSection.tvDescription.text =
            jobDetails.description

        //Details section
        activityJobDetailsBinding.jobDetailsDetailsSection.tvQualification.text =
            jobDetails.qualification.toString()
        activityJobDetailsBinding.jobDetailsDetailsSection.tvIndustry.text =
            jobDetails.industry
        activityJobDetailsBinding.jobDetailsDetailsSection.tvSalary.text =
            jobDetails.salary.toString()

        if (jobDetails.isOpenForDisability) {
            activityJobDetailsBinding.jobDetailsDescriptionSection.tvAdditionalInformation.visibility =
                View.VISIBLE

            activityJobDetailsBinding.jobDetailsDescriptionSection.tvAdditionalInformation.text =
                resources.getString(
                    R.string.job_details_additional_information,
                    jobData.additionalInformation
                )
        } else {
            activityJobDetailsBinding.jobDetailsDescriptionSection.tvAdditionalInformation.visibility =
                View.GONE
        }
    }

    private fun getApplicationsByJob(jobId: String) {
        val factory = ViewModelFactory.getInstance(this)

        applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]

        applicationViewModel = ViewModelProvider(this, factory)[ApplicationViewModel::class.java]
        applicationAdapter = ApplicationAdapter(this, this, this)
        applicationViewModel.getApplicationsByJob(jobId).observe(this) { applications ->
            if (applications.data != null) {
                when (applications.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (applications.data.isNotEmpty()) {
                            applicationAdapter.submitList(applications.data)
                            applicationAdapter.notifyDataSetChanged()
                            activityJobDetailsBinding.jobDetailsApplicantsSection.tvNoData.visibility =
                                View.GONE
                        } else {
                            activityJobDetailsBinding.jobDetailsApplicantsSection.tvNoData.visibility =
                                View.VISIBLE
                        }
                        activityJobDetailsBinding.jobDetailsTitleSection.tvJobPostedDateAndApplicants.text =
                            resources.getString(
                                R.string.job_details_date_format,
                                DateUtils.getPostedDate(jobData.postedDate),
                                applicationAdapter.itemCount
                            )
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        with(activityJobDetailsBinding.jobDetailsApplicantsSection.rvApplication) {
            layoutManager = LinearLayoutManager(this@JobDetailsActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@JobDetailsActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = applicationAdapter
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
                val intent = Intent(this, CreateEditJobActivity::class.java)
                intent.putExtra(CreateEditJobActivity.JOB_EXTRA, jobData)
                startActivityForResult(intent, CreateEditJobActivity.REQUEST_UPDATE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreateEditJobActivity.REQUEST_UPDATE) {
            if (resultCode == CreateEditJobActivity.RESULT_UPDATE) {
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

    override fun onLoadApplicantDetailsCallback(
        applicantId: String,
        callback: ApplicationAdapter.LoadApplicantDataCallback
    ) {
        applicantViewModel.getApplicantDetails(applicantId).observe(this) { applicantDetails ->
            if (applicantDetails.data != null) {
                callback.onGetApplicantDetails(applicantDetails.data)
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
        val intent = Intent(this, ApplicationDetailsActivity::class.java)
        intent.putExtra(ApplicationDetailsActivity.APPLICATION_ID, applicationId)
        intent.putExtra(ApplicationDetailsActivity.JOB_ID, jobId)
        intent.putExtra(ApplicationDetailsActivity.APPLICANT_ID, applicantId)
        startActivity(intent)
    }
}