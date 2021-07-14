package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.ActivityJobDetailsBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.applicant.LoadApplicantDataCallback
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationAdapter
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationDetailsActivity
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.callback.DeleteApplicationByJobCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.OnApplicationClickCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.SendApplicationDataCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.DeleteJobCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.DeleteSavedJobCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.LoadJobDataCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.DateUtils
import org.d3ifcool.dissajobrecruiter.vo.Status
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class JobDetailsActivity : AppCompatActivity(),
    LoadApplicantDataCallback, DeleteJobCallback, OnApplicationClickCallback,
    LoadJobDataCallback, DeleteApplicationByJobCallback, DeleteSavedJobCallback,
    View.OnClickListener, SendApplicationDataCallback {

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

    private lateinit var jobId: String

    private val csv = StringBuilder()

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityJobDetailsBinding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(activityJobDetailsBinding.root)

        activityJobDetailsBinding.toolbar.title = resources.getString(R.string.job_details)
        setSupportActionBar(activityJobDetailsBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        csv.append("No,Nama,Email,Nomor Telepon,Tentang,Melamar Sebagai,Tanggal Melamar,Status Lamaran")

        val extras = intent.extras
        if (extras != null) {
            val factory = ViewModelFactory.getInstance(this)
            jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            jobId = extras.getString(EXTRA_ID).toString()
            showJobDetails(jobId)
            getApplicationsByJob(jobId)
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
        applicationAdapter = ApplicationAdapter(this, this, this, this)
        applicationViewModel.getApplicationsByJob(jobId).observe(this) { applications ->
            if (applications.data != null) {
                when (applications.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        if (applications.data.isNotEmpty()) {
                            applicationAdapter.submitList(applications.data)
                            applicationAdapter.notifyDataSetChanged()
                            showRecyclerView(true)
                        } else {
                            showRecyclerView(false)
                        }
                        activityJobDetailsBinding.jobDetailsTitleSection.tvJobPostedDateAndApplicants.text =
                            resources.getString(
                                R.string.job_details_date_format,
                                DateUtils.getPostedDate(jobData.postedDate),
                                applicationAdapter.itemCount
                            )
                    }
                    Status.ERROR -> {
                        showRecyclerView(false)
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

    private fun showLoading(state: Boolean) {
        if (state) {
            activityJobDetailsBinding.jobDetailsApplicantsSection.progressBar.visibility =
                View.VISIBLE
        } else {
            activityJobDetailsBinding.jobDetailsApplicantsSection.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView(state: Boolean) {
        if (state) {
            activityJobDetailsBinding.jobDetailsApplicantsSection.rvApplication.visibility =
                View.VISIBLE
            activityJobDetailsBinding.jobDetailsApplicantsSection.tvNoData.visibility = View.GONE
            activityJobDetailsBinding.jobDetailsApplicantsSection.btnExportApplicants.visibility =
                View.VISIBLE

            activityJobDetailsBinding.jobDetailsApplicantsSection.btnExportApplicants.setOnClickListener(this)
        } else {
            activityJobDetailsBinding.jobDetailsApplicantsSection.rvApplication.visibility =
                View.GONE
            activityJobDetailsBinding.jobDetailsApplicantsSection.tvNoData.visibility = View.VISIBLE
            activityJobDetailsBinding.jobDetailsApplicantsSection.btnExportApplicants.visibility =
                View.GONE
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

    override fun onSuccessDeleteJob() {
        jobViewModel.deleteSavedJobByJob(jobId, this)
    }

    override fun onFailureDeleteJob(messageId: Int) {
        dialog.dismissWithAnimation()
        Toast.makeText(
            this,
            resources.getString(messageId),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onSuccessDeleteSavedJob() {
        applicationViewModel.deleteApplicationsByJob(jobId, this)
    }

    override fun onFailureDeleteSavedJob(messageId: Int) {
        dialog.dismissWithAnimation()
        Toast.makeText(
            this,
            resources.getString(messageId),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onSuccessDeleteApplications() {
        dialog.dismissWithAnimation()
        Toast.makeText(
            this,
            resources.getString(R.string.success_alert_delete_job),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    override fun onFailureDeleteApplications(messageId: Int) {
        dialog.dismissWithAnimation()
        Toast.makeText(
            this,
            resources.getString(messageId),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onLoadApplicantDetailsCallback(
        applicantId: String,
        callback: LoadApplicantDataCallback
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
    }

    override fun onItemClick(applicationId: String, jobId: String, applicantId: String) {
        val intent = Intent(this, ApplicationDetailsActivity::class.java)
        intent.putExtra(ApplicationDetailsActivity.APPLICATION_ID, applicationId)
        intent.putExtra(ApplicationDetailsActivity.JOB_ID, jobId)
        intent.putExtra(ApplicationDetailsActivity.APPLICANT_ID, applicantId)
        startActivity(intent)
    }

    private fun exportApplicationToCsv() {
        try {
            val fo: FileOutputStream = openFileOutput("Daftar Pelamar ${jobData.title}.csv", Context.MODE_PRIVATE)
            fo.write(csv.toString().toByteArray())
            fo.close()

            //exporting
            val context = applicationContext
            val filelocation = File(filesDir, "Daftar Pelamar ${jobData.title}.csv")
            val path: Uri = FileProvider.getUriForFile(
                context,
                "org.d3ifcool.dissajobrecruiter.fileprovider",
                filelocation
            )
            val fileIntent = Intent(Intent.ACTION_SEND)
            fileIntent.type = "text/csv"
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Daftar Pelamar ${jobData.title}")
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            fileIntent.putExtra(Intent.EXTRA_STREAM, path)
            startActivity(Intent.createChooser(fileIntent, "Send mail"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnExportApplicants -> exportApplicationToCsv()
        }
    }

    override fun sendApplicationAndApplicantData(
        applicationEntity: ApplicationEntity,
        applicantEntity: ApplicantEntity,
        jobDetailsEntity: JobDetailsEntity
    ) {
        if (count != applicationAdapter.itemCount) {
            count++
            csv.append("\n$count,${applicantEntity.fullName},${applicantEntity.email},${applicantEntity.phoneNumber},${applicantEntity.aboutMe},${jobDetailsEntity.title},${applicationEntity.applyDate.toString()},${applicationEntity.status}")
        }
    }
}