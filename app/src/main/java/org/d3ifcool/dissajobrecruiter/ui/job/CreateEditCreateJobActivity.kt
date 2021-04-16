package org.d3ifcool.dissajobrecruiter.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.databinding.ActivityCreateEditJobBinding
import org.d3ifcool.dissajobrecruiter.ui.job.callback.CreateJobCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.UpdateJobCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class CreateEditCreateJobActivity : AppCompatActivity(), View.OnClickListener, CreateJobCallback, UpdateJobCallback,
    CompoundButton.OnCheckedChangeListener {

    companion object {
        const val JOB_EXTRA = "job_extra"
        const val REQUEST_UPDATE = 100
        const val RESULT_UPDATE = 101
    }

    private lateinit var activityCreateEditJobBinding: ActivityCreateEditJobBinding

    private lateinit var viewModel: JobViewModel

    private lateinit var dialog: SweetAlertDialog

    private lateinit var spEmploymentAdapter: ArrayAdapter<CharSequence>

    private lateinit var spIndustryAdapter: ArrayAdapter<CharSequence>

    private lateinit var jobId: String

    private lateinit var jobLastPostedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCreateEditJobBinding = ActivityCreateEditJobBinding.inflate(layoutInflater)
        setContentView(activityCreateEditJobBinding.root)

        spinnerInit()

        if (intent.extras != null) {
            activityCreateEditJobBinding.header.tvHeader.text = resources.getString(R.string.Edit_job)
            showPreviousJobData()
            activityCreateEditJobBinding.btnSubmitJob.text = resources.getString(R.string.update)
        } else {
            activityCreateEditJobBinding.header.tvHeader.text = resources.getString(R.string.create_job)
            activityCreateEditJobBinding.btnSubmitJob.text = resources.getString(R.string.publish)
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)

        activityCreateEditJobBinding.header.imgBackBtn.setOnClickListener(this)
        activityCreateEditJobBinding.btnSubmitJob.setOnClickListener(this)
    }

    private fun showPreviousJobData() {
        activityCreateEditJobBinding.tvJobStatusTitle.visibility = View.VISIBLE
        activityCreateEditJobBinding.scJobStatus.visibility = View.VISIBLE

        val jobData = intent.getParcelableExtra<JobDetailsEntity>(JOB_EXTRA)

        jobId = jobData?.id.toString()
        jobLastPostedDate = jobData?.postedDate.toString()

        activityCreateEditJobBinding.etTitle.setText(jobData?.title.toString())
        activityCreateEditJobBinding.etDescription.setText(jobData?.description.toString())
        activityCreateEditJobBinding.etQualification.setText(jobData?.qualification.toString())
        activityCreateEditJobBinding.spEmployment.setSelection(spEmploymentAdapter.getPosition(jobData?.employment.toString()))
        activityCreateEditJobBinding.spIndustry.setSelection(spIndustryAdapter.getPosition(jobData?.industry.toString()))
        activityCreateEditJobBinding.etSalary.setText(jobData?.salary.toString())

        activityCreateEditJobBinding.scJobStatus.isChecked = jobData?.isOpen.toString().toBoolean()
        if(jobData?.isOpen == true) {
            activityCreateEditJobBinding.scJobStatus.text = resources.getString(R.string.open_job)
        } else {
            activityCreateEditJobBinding.scJobStatus.text = resources.getString(R.string.closed_job)
        }

        activityCreateEditJobBinding.scJobStatus.setOnCheckedChangeListener(this)
    }

    private fun spinnerInit() {
        ArrayAdapter.createFromResource(this, R.array.employment, R.layout.spinner_items)
            .also { adapter ->
                spEmploymentAdapter = adapter
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                activityCreateEditJobBinding.spEmployment.adapter = adapter
            }

        ArrayAdapter.createFromResource(this, R.array.industry, R.layout.spinner_items)
            .also { adapter ->
                spIndustryAdapter = adapter
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                activityCreateEditJobBinding.spIndustry.adapter = adapter
            }
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityCreateEditJobBinding.etTitle.text.toString().trim())) {
            activityCreateEditJobBinding.etTitle.error =
                resources.getString(R.string.error_alert, "Title")
            return
        }

        if (TextUtils.isEmpty(activityCreateEditJobBinding.etDescription.text.toString().trim())) {
            activityCreateEditJobBinding.etDescription.error =
                resources.getString(R.string.error_alert, "Deskripsi")
            return
        }

        if (TextUtils.isEmpty(activityCreateEditJobBinding.etQualification.text.toString().trim())) {
            activityCreateEditJobBinding.etQualification.error =
                resources.getString(R.string.error_alert, "Kualifikasi")
            return
        }

        storeToDatabase()

        dialog.titleText = resources.getString(R.string.loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun storeToDatabase() {

        val title = activityCreateEditJobBinding.etTitle.text.toString().trim()
        val description = activityCreateEditJobBinding.etDescription.text.toString().trim()
        val qualification = activityCreateEditJobBinding.etQualification.text.toString().trim()
        val employment = activityCreateEditJobBinding.spEmployment.selectedItem.toString().trim()
        val industry = activityCreateEditJobBinding.spIndustry.selectedItem.toString().trim()

        var salary = 0

        if (!TextUtils.isEmpty(activityCreateEditJobBinding.etSalary.text.toString().trim())) {
            salary = activityCreateEditJobBinding.etSalary.text.toString().trim().toInt()
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate: String = sdf.format(Date())

        val job = JobDetailsResponseEntity(
            id = "",
            title = title,
            description = description,
            qualification = qualification,
            employment = employment,
            industry = industry,
            salary = salary,
            updatedDate = currentDate
        )

        if(intent.extras != null) {
            job.id = jobId
            job.postedDate = jobLastPostedDate
            job.updatedDate = currentDate
            job.isOpen = activityCreateEditJobBinding.scJobStatus.isChecked
            if (job.isOpen == false) {
                job.closedDate = currentDate
            }
            viewModel.updateJob(job, this)
        } else {
            job.isOpen = true
            job.postedDate = currentDate
            viewModel.createJob(job, this)
        }
    }

    override fun onSuccess() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        if(intent.extras != null) {
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBackBtn -> finish()
            R.id.btnSubmitJob -> formValidation()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(isChecked) {
            true -> buttonView?.text = resources.getString(R.string.open_job)
            false -> buttonView?.text = resources.getString(R.string.closed_job)
        }
    }
}