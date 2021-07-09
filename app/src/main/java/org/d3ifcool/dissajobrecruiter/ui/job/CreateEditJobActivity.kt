package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.databinding.ActivityCreateEditJobBinding
import org.d3ifcool.dissajobrecruiter.ui.job.callback.CreateJobCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.UpdateJobCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.CheckRecruiterDataCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.RecruiterViewModel
import org.d3ifcool.dissajobrecruiter.ui.settings.ChangePhoneNumberActivity
import org.d3ifcool.dissajobrecruiter.ui.settings.ChangeProfileActivity
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.DateUtils

class CreateEditJobActivity : AppCompatActivity(), View.OnClickListener, CreateJobCallback,
    UpdateJobCallback, RadioGroup.OnCheckedChangeListener, CheckRecruiterDataCallback {

    companion object {
        const val JOB_EXTRA = "job_extra"
        const val REQUEST_UPDATE = 100
        const val RESULT_UPDATE = 101
    }

    private lateinit var activityCreateEditJobBinding: ActivityCreateEditJobBinding

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var jobViewModel: JobViewModel

    private lateinit var dialog: SweetAlertDialog

    private lateinit var jobId: String

    private lateinit var jobLastPostedDate: String

    private var isBtnClicked = false

    private val recruiterId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCreateEditJobBinding = ActivityCreateEditJobBinding.inflate(layoutInflater)
        setContentView(activityCreateEditJobBinding.root)

        if (intent.extras != null) {
            showPreviousJobData()
            activityCreateEditJobBinding.toolbar.title =
                resources.getString(R.string.edit_job_title)
            activityCreateEditJobBinding.btnSubmitJob.text =
                resources.getString(R.string.txt_update)
        } else {
            activityCreateEditJobBinding.toolbar.title =
                resources.getString(R.string.create_job_title)
            activityCreateEditJobBinding.btnSubmitJob.text = resources.getString(R.string.publish)
        }

        setSupportActionBar(activityCreateEditJobBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
        jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)

        activityCreateEditJobBinding.etJobEmploymentType.setOnClickListener(this)
        activityCreateEditJobBinding.etJobIndustryType.setOnClickListener(this)
        activityCreateEditJobBinding.etJobType.setOnClickListener(this)
        activityCreateEditJobBinding.btnSubmitJob.setOnClickListener(this)

        activityCreateEditJobBinding.rgOpenForDisability.setOnCheckedChangeListener(this)
    }

    private fun showPreviousJobData() {
        activityCreateEditJobBinding.tvJobStatus.visibility = View.VISIBLE
        activityCreateEditJobBinding.rgJobStatus.visibility = View.VISIBLE

        val jobData = intent.getParcelableExtra<JobDetailsEntity>(JOB_EXTRA)

        jobId = jobData?.id.toString()
        jobLastPostedDate = jobData?.postedDate.toString()

        activityCreateEditJobBinding.etJobTitle.setText(jobData?.title.toString())
        activityCreateEditJobBinding.etJobDescription.setText(jobData?.description.toString())
        activityCreateEditJobBinding.etJobAddress.setText(jobData?.address.toString())
        activityCreateEditJobBinding.etJobQualification.setText(jobData?.qualification.toString())
        activityCreateEditJobBinding.etJobEmploymentType.setText(jobData?.employment.toString())
        activityCreateEditJobBinding.etJobIndustryType.setText(jobData?.industry.toString())
        activityCreateEditJobBinding.etJobType.setText(jobData?.type.toString())
        activityCreateEditJobBinding.etJobSalary.setText(jobData?.salary.toString())


        if (jobData?.isOpen?.toString().toBoolean()) {
            activityCreateEditJobBinding.rbOpenJob.isChecked = true
        } else {
            activityCreateEditJobBinding.rbCloseJob.isChecked = true
        }

        if (jobData?.isOpenForDisability?.toString().toBoolean()) {
            activityCreateEditJobBinding.rbOpenForDisability.isChecked = true
            activityCreateEditJobBinding.tvAdditionalInformation.visibility = View.VISIBLE
            activityCreateEditJobBinding.etAdditionalInformation.visibility = View.VISIBLE
            activityCreateEditJobBinding.etAdditionalInformation.setText(jobData?.additionalInformation)
        } else {
            activityCreateEditJobBinding.rbCloseForDisability.isChecked = true
            activityCreateEditJobBinding.tvAdditionalInformation.visibility = View.GONE
            activityCreateEditJobBinding.etAdditionalInformation.visibility = View.GONE
        }
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityCreateEditJobBinding.etJobTitle.text.toString().trim())) {
            activityCreateEditJobBinding.etJobTitle.error =
                resources.getString(R.string.txt_edit_text_error_alert, "Kolom")
            return
        }

        if (TextUtils.isEmpty(
                activityCreateEditJobBinding.etJobDescription.text.toString().trim()
            )
        ) {
            activityCreateEditJobBinding.etJobDescription.error =
                resources.getString(R.string.txt_edit_text_error_alert, "Kolom")
            return
        }

        if (TextUtils.isEmpty(activityCreateEditJobBinding.etJobAddress.text.toString().trim())) {
            activityCreateEditJobBinding.etJobAddress.error =
                resources.getString(R.string.txt_edit_text_error_alert, "Kolom")
            return
        }

        if (TextUtils.isEmpty(
                activityCreateEditJobBinding.etJobQualification.text.toString().trim()
            )
        ) {
            activityCreateEditJobBinding.etJobQualification.error =
                resources.getString(R.string.txt_edit_text_error_alert, "Kolom")
            return
        }

        if (TextUtils.isEmpty(
                activityCreateEditJobBinding.etJobEmploymentType.text.toString().trim()
            )
        ) {
            activityCreateEditJobBinding.etJobEmploymentType.error =
                resources.getString(R.string.txt_edit_text_error_alert, "Kolom")
            return
        }

        if (TextUtils.isEmpty(
                activityCreateEditJobBinding.etJobIndustryType.text.toString().trim()
            )
        ) {
            activityCreateEditJobBinding.etJobIndustryType.error =
                resources.getString(R.string.txt_edit_text_error_alert, "Kolom")
            return
        }

        if (TextUtils.isEmpty(
                activityCreateEditJobBinding.etJobType.text.toString().trim()
            )
        ) {
            activityCreateEditJobBinding.etJobType.error =
                resources.getString(R.string.txt_edit_text_error_alert, "Kolom")
            return
        }

        if (activityCreateEditJobBinding.etAdditionalInformation.visibility == View.VISIBLE) {
            if (TextUtils.isEmpty(
                    activityCreateEditJobBinding.etAdditionalInformation.text.toString().trim()
                )
            ) {
                activityCreateEditJobBinding.etAdditionalInformation.error =
                    resources.getString(R.string.txt_edit_text_error_alert, "Kolom")
                return
            }
        }

        storeToDatabase()

        dialog.titleText = resources.getString(R.string.loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun storeToDatabase() {

        val title = activityCreateEditJobBinding.etJobTitle.text.toString().trim()
        val description = activityCreateEditJobBinding.etJobDescription.text.toString().trim()
        val address = activityCreateEditJobBinding.etJobAddress.text.toString().trim()
        val qualification = activityCreateEditJobBinding.etJobQualification.text.toString().trim()
        val employment = activityCreateEditJobBinding.etJobEmploymentType.text.toString().trim()
        val industry = activityCreateEditJobBinding.etJobIndustryType.text.toString().trim()
        val type = activityCreateEditJobBinding.etJobType.text.toString().trim()

        var salary = resources.getString(R.string.txt_job_salary_value)

        if (!TextUtils.isEmpty(activityCreateEditJobBinding.etJobSalary.text.toString().trim())) {
            salary = activityCreateEditJobBinding.etJobSalary.text.toString().trim()
        }

        val isOpenForDisability = activityCreateEditJobBinding.rbOpenForDisability.isChecked

        val additionalInformation = if (isOpenForDisability) {
            activityCreateEditJobBinding.etAdditionalInformation.text.toString().trim()
        } else {
            "-"
        }

        val currentDate: String = DateUtils.getCurrentDate()

        val job = JobDetailsResponseEntity(
            "",
            title,
            description,
            address,
            qualification,
            employment,
            industry,
            type,
            salary,
            recruiterId,
            currentDate,
            "-",
            "-",
            true,
            isOpenForDisability,
            additionalInformation
        )

        if (intent.extras != null) {
            job.id = jobId
            job.postedDate = jobLastPostedDate
            job.updatedDate = currentDate
            job.isOpen =
                activityCreateEditJobBinding.rgJobStatus.checkedRadioButtonId == R.id.rbOpenJob
            job.isOpenForDisability =
                activityCreateEditJobBinding.rgOpenForDisability.checkedRadioButtonId == R.id.rbOpenForDisability
            if (!job.isOpen) {
                job.closedDate = currentDate
            }
            jobViewModel.updateJob(job, this)
        } else {
            job.isOpen = true
            job.postedDate = currentDate
            jobViewModel.createJob(job, this)
        }
    }

    private fun showToast() =
        Toast.makeText(
            this,
            resources.getString(R.string.txt_fill_all_data_alert),
            Toast.LENGTH_SHORT
        ).show()

    override fun onSuccess() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        if (intent.extras != null) {
            dialog.titleText = resources.getString(R.string.success_update_job)
        } else {
            dialog.titleText = resources.getString(R.string.success_post_job)
        }
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            if (intent.extras != null) {
                setResult(RESULT_UPDATE)
            }
            finish()
        }
        dialog.show()
    }

    override fun onFailure(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSubmitJob -> {
                isBtnClicked = true
                recruiterViewModel.checkRecruiterData(recruiterId, this)
            }
            R.id.etJobEmploymentType -> {
                startActivityForResult(
                    Intent(
                        this,
                        EmploymentTypeActivity::class.java
                    ), EmploymentTypeActivity.REQUEST_EMPLOYMENT_TYPE
                )
            }
            R.id.etJobIndustryType -> {
                startActivityForResult(
                    Intent(
                        this,
                        IndustryTypeActivity::class.java
                    ), IndustryTypeActivity.REQUEST_INDUSTRY_TYPE
                )
            }
            R.id.etJobType -> {
                startActivityForResult(
                    Intent(
                        this,
                        JobTypeActivity::class.java
                    ), JobTypeActivity.REQUEST_JOB_TYPE
                )
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rbOpenForDisability -> {
                activityCreateEditJobBinding.tvAdditionalInformation.visibility = View.VISIBLE
                activityCreateEditJobBinding.etAdditionalInformation.visibility = View.VISIBLE
            }
            R.id.rbCloseForDisability -> {
                activityCreateEditJobBinding.tvAdditionalInformation.visibility = View.GONE
                activityCreateEditJobBinding.etAdditionalInformation.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EmploymentTypeActivity.REQUEST_EMPLOYMENT_TYPE) {
            if (resultCode == EmploymentTypeActivity.RESULT_EMPLOYMENT_TYPE) {
                activityCreateEditJobBinding.etJobEmploymentType.setText(
                    data?.getStringExtra(
                        EmploymentTypeActivity.SELECTED_EMPLOYMENT_TYPE
                    )
                )
            }
        } else if (requestCode == IndustryTypeActivity.REQUEST_INDUSTRY_TYPE) {
            if (resultCode == IndustryTypeActivity.RESULT_INDUSTRY_TYPE) {
                activityCreateEditJobBinding.etJobIndustryType.setText(
                    data?.getStringExtra(
                        IndustryTypeActivity.SELECTED_INDUSTRY_TYPE
                    )
                )
            }
        } else if (requestCode == JobTypeActivity.REQUEST_JOB_TYPE) {
            if (resultCode == JobTypeActivity.RESULT_JOB_TYPE) {
                activityCreateEditJobBinding.etJobType.setText(
                    data?.getStringExtra(
                        JobTypeActivity.SELECTED_JOB_TYPE
                    )
                )
            }
        }
    }

    override fun allDataAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            formValidation()
        }
    }

    override fun profileDataNotAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            showToast()
            startActivity(Intent(this, ChangeProfileActivity::class.java))
        }
    }

    override fun phoneNumberNotAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            showToast()
            startActivity(Intent(this, ChangePhoneNumberActivity::class.java))
        }
    }
}