package org.d3ifcool.dissajobrecruiter.ui.application

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityApplicationDetailsBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantProfileActivity
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.callback.UpdateApplicationMarkCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.UpdateApplicationStatusCallback
import org.d3ifcool.dissajobrecruiter.ui.job.JobDetailsActivity
import org.d3ifcool.dissajobrecruiter.ui.job.JobViewModel
import org.d3ifcool.dissajobrecruiter.ui.question.InterviewViewModel
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class ApplicationDetailsActivity : AppCompatActivity(), View.OnClickListener,
    UpdateApplicationStatusCallback, UpdateApplicationMarkCallback {

    companion object {
        const val APPLICATION_ID = "application_id"
        const val JOB_ID = "job_id"
        const val APPLICANT_ID = "applicant_id"
    }

    private lateinit var activityApplicationDetailsBinding: ActivityApplicationDetailsBinding

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var applicationViewModel: ApplicationViewModel

    private lateinit var jobViewModel: JobViewModel

    private lateinit var interviewViewModel: InterviewViewModel

    private lateinit var dialog: SweetAlertDialog

    private lateinit var applicationId: String
    private lateinit var applicantId: String
    private lateinit var jobId: String

    private var isApplicationMarked = false
    private var isApplicationAccepted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApplicationDetailsBinding =
            ActivityApplicationDetailsBinding.inflate(layoutInflater)
        setContentView(activityApplicationDetailsBinding.root)

        activityApplicationDetailsBinding.toolbar.title =
            resources.getString(R.string.txt_application_details)
        setSupportActionBar(activityApplicationDetailsBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        applicationId = intent.getStringExtra(APPLICATION_ID).toString()
        applicantId = intent.getStringExtra(APPLICANT_ID).toString()
        jobId = intent.getStringExtra(JOB_ID).toString()

        val factory = ViewModelFactory.getInstance(this)
        applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
        applicationViewModel = ViewModelProvider(this, factory)[ApplicationViewModel::class.java]
        jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
        interviewViewModel = ViewModelProvider(this, factory)[InterviewViewModel::class.java]

        applicantViewModel.getApplicantDetails(applicantId).observe(this) { profile ->
            if (profile.data != null) {
                when (profile.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> populateApplicantData(
                        profile.data.fullName.toString(),
                        profile.data.imagePath.toString()
                    )
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        applicationViewModel.getApplicationById(applicationId)
            .observe(this) { application ->
                if (application.data != null) {
                    when (application.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> populateApplicationData(
                            application.data.applyDate.toString(),
                            application.data.status.toString()
                        )
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        jobViewModel.getJobDetails(jobId).observe(this) { jobDetails ->
            if (jobDetails.data != null) {
                when (jobDetails.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        populateJobData(jobDetails.data.title, jobDetails.data.description)
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        interviewViewModel.getInterviewAnswers(applicationId)
            .observe(this) { interview ->
                if (interview.data != null) {
                    when (interview.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> populateInterviewData(
                            interview.data.firstAnswer.toString(),
                            interview.data.secondAnswer.toString(),
                            interview.data.thirdAnswer.toString()
                        )
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        activityApplicationDetailsBinding.applicantProfileSection.root.setOnClickListener(this)
        activityApplicationDetailsBinding.applicationDetailsSection.root.setOnClickListener(this)
        activityApplicationDetailsBinding.footerSection.btnRejectApplication.setOnClickListener(this)
        activityApplicationDetailsBinding.footerSection.btnAcceptApplication.setOnClickListener(this)
    }

    private fun populateApplicantData(applicantName: String, imagePath: String) {
        activityApplicationDetailsBinding.applicantProfileSection.tvApplicantName.text =
            applicantName

        if (imagePath != "-") {
            val storageRef = Firebase.storage.reference
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(this)
                .load(storageRef.child("applicant/profile/images/$imagePath"))
                .transform(RoundedCorners(20))
                .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                .error(R.drawable.ic_profile_gray_24dp)
                .into(activityApplicationDetailsBinding.applicantProfileSection.imgApplicantPicture)
        }

        activityApplicationDetailsBinding.applicantProfileSection.tvShowProfile.setOnClickListener(
            this
        )
    }

    private fun populateApplicationData(applyDate: String, status: String) {

        activityApplicationDetailsBinding.applicationDetailsSection.tvApplyDate.text = applyDate

        when (status) {
            "Waiting" -> {
                activityApplicationDetailsBinding.applicationDetailsSection.tvApplicationStatus.setTextColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
                )
            }
            "Accepted" -> {
                activityApplicationDetailsBinding.applicationDetailsSection.tvApplicationStatus.setTextColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))
                )
            }
            "Rejected" -> {
                activityApplicationDetailsBinding.applicationDetailsSection.tvApplicationStatus.setTextColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
                )
            }
        }
        activityApplicationDetailsBinding.applicationDetailsSection.tvApplicationStatus.text =
            status
    }

    private fun populateJobData(jobTitle: String, jobDescription: String) {
        activityApplicationDetailsBinding.applicationDetailsSection.tvJobTitle.text = jobTitle
        activityApplicationDetailsBinding.applicationDetailsSection.tvJobDescription.text =
            jobDescription
    }

    private fun populateInterviewData(
        firstAnswer: String,
        secondAnswer: String,
        thirdAnswer: String
    ) {
        activityApplicationDetailsBinding.additionalInformationSection.etFirstQuestion.setText(
            firstAnswer
        )
        activityApplicationDetailsBinding.additionalInformationSection.etSecondQuestion.setText(
            secondAnswer
        )
        activityApplicationDetailsBinding.additionalInformationSection.etThirdQuestion.setText(
            thirdAnswer
        )
    }

    private fun updateApplicationMark() {
        applicationViewModel.updateApplicationMark(applicationId, !isApplicationMarked, this)
    }

    private fun updateApplicationStatus(status: String) {
        activityApplicationDetailsBinding.footerSection.btnRejectApplication.isEnabled = false
        activityApplicationDetailsBinding.footerSection.btnAcceptApplication.isEnabled = false

        val dialogTitle = if (status == "Accepted") {
            resources.getString(R.string.txt_accept)
        } else {
            resources.getString(R.string.txt_reject)
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(R.string.txt_update_application_alert, dialogTitle)
        dialog.setCancelable(true)
        dialog.cancelText = resources.getString(R.string.txt_cancel)
        dialog.setConfirmClickListener {
            dialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
            dialog.titleText = resources.getString(R.string.loading)
            dialog.showCancelButton(false)
            dialog.setCancelable(false)
            applicationViewModel.updateApplicationStatus(applicationId, status, this)

        }.setOnCancelListener {
            activityApplicationDetailsBinding.footerSection.btnRejectApplication.isEnabled = true
            activityApplicationDetailsBinding.footerSection.btnAcceptApplication.isEnabled = true
        }
        dialog.show()
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessUpdateStatus() {
        dialog.dismissWithAnimation()
        if (isApplicationAccepted) {
            showToast(resources.getString(R.string.txt_accept_application))
        } else {
            showToast(resources.getString(R.string.txt_reject_application))
        }
        activityApplicationDetailsBinding.footerSection.btnRejectApplication.isEnabled = false
        activityApplicationDetailsBinding.footerSection.btnAcceptApplication.isEnabled = false
    }

    override fun onFailureUpdateStatus(messageId: Int) {
        dialog.dismissWithAnimation()
        showToast(resources.getString(messageId))
        activityApplicationDetailsBinding.footerSection.btnRejectApplication.isEnabled = true
        activityApplicationDetailsBinding.footerSection.btnAcceptApplication.isEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.application_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.markApplication -> {
                updateApplicationMark()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvShowProfile -> {
                val intent = Intent(this, ApplicantProfileActivity::class.java)
                intent.putExtra(ApplicantProfileActivity.APPLICANT_ID, applicantId)
                startActivity(intent)
            }
            R.id.applicationDetailsSection -> {
                val intent = Intent(this, JobDetailsActivity::class.java)
                intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
                startActivity(intent)
            }

            R.id.btnRejectApplication -> updateApplicationStatus("Rejected")
            R.id.btnAcceptApplication -> updateApplicationStatus("Accepted")
        }
    }

    override fun onSuccessUpdateMark() {
        isApplicationMarked = !isApplicationMarked
        showToast(resources.getString(R.string.txt_mark_application))
    }

    override fun onFailureUpdateMark(messageId: Int) {
        showToast(resources.getString(messageId))
    }
}