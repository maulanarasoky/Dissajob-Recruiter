package org.d3ifcool.dissajobrecruiter.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
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

    private lateinit var spJobEmploymentAdapter: ArrayAdapter<CharSequence>

    private lateinit var spJobTypeAdapter: ArrayAdapter<CharSequence>

    private lateinit var spJobIndustryAdapter: ArrayAdapter<CharSequence>

    private lateinit var jobId: String

    private lateinit var jobLastPostedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCreateEditJobBinding = ActivityCreateEditJobBinding.inflate(layoutInflater)
        setContentView(activityCreateEditJobBinding.root)

        spinnerInit()

        if (intent.extras != null) {
            showPreviousJobData()
            activityCreateEditJobBinding.toolbar.title = resources.getString(R.string.edit_job_title)
            activityCreateEditJobBinding.btnSubmitJob.text = resources.getString(R.string.update)
        } else {
            activityCreateEditJobBinding.toolbar.title = resources.getString(R.string.create_job_title)
            activityCreateEditJobBinding.btnSubmitJob.text = resources.getString(R.string.publish)
        }

        setSupportActionBar(activityCreateEditJobBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)

        activityCreateEditJobBinding.btnSubmitJob.setOnClickListener(this)
    }

    private fun showPreviousJobData() {
        activityCreateEditJobBinding.tvJobStatusTitle.visibility = View.VISIBLE
        activityCreateEditJobBinding.scJobStatus.visibility = View.VISIBLE

        val jobData = intent.getParcelableExtra<JobDetailsEntity>(JOB_EXTRA)

        jobId = jobData?.id.toString()
        jobLastPostedDate = jobData?.postedDate.toString()

        activityCreateEditJobBinding.etJobTitle.setText(jobData?.title.toString())
        activityCreateEditJobBinding.etJobDescription.setText(jobData?.description.toString())
        activityCreateEditJobBinding.etJobAddress.setText(jobData?.address.toString())
        activityCreateEditJobBinding.etJobQualification.setText(jobData?.qualification.toString())
        activityCreateEditJobBinding.spJobEmployment.setSelection(spJobEmploymentAdapter.getPosition(jobData?.employment.toString()))
        activityCreateEditJobBinding.spJobType.setSelection(spJobTypeAdapter.getPosition(jobData?.type.toString()))
        activityCreateEditJobBinding.spJobIndustry.setSelection(spJobIndustryAdapter.getPosition(jobData?.industry.toString()))
        activityCreateEditJobBinding.etJobSalary.setText(jobData?.salary.toString())

        activityCreateEditJobBinding.scJobStatus.isChecked = jobData?.isOpen.toString().toBoolean()
        if(jobData?.isOpen == true) {
            activityCreateEditJobBinding.scJobStatus.text = resources.getString(R.string.open_job)
        } else {
            activityCreateEditJobBinding.scJobStatus.text = resources.getString(R.string.closed_job)
        }

        activityCreateEditJobBinding.scJobStatus.setOnCheckedChangeListener(this)
    }

    private fun spinnerInit() {
        ArrayAdapter.createFromResource(this, R.array.arr_job_employment, R.layout.spinner_items)
            .also { adapter ->
                spJobEmploymentAdapter = adapter
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                activityCreateEditJobBinding.spJobEmployment.adapter = adapter
            }

        ArrayAdapter.createFromResource(this, R.array.arr_job_type, R.layout.spinner_items)
            .also { adapter ->
                spJobTypeAdapter = adapter
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                activityCreateEditJobBinding.spJobType.adapter = adapter
            }

        ArrayAdapter.createFromResource(this, R.array.arr_job_industry, R.layout.spinner_items)
            .also { adapter ->
                spJobIndustryAdapter = adapter
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                activityCreateEditJobBinding.spJobIndustry.adapter = adapter
            }
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityCreateEditJobBinding.etJobTitle.text.toString().trim())) {
            activityCreateEditJobBinding.etJobTitle.error =
                resources.getString(R.string.error_alert, "Title")
            return
        }

        if (TextUtils.isEmpty(activityCreateEditJobBinding.etJobDescription.text.toString().trim())) {
            activityCreateEditJobBinding.etJobDescription.error =
                resources.getString(R.string.error_alert, "Deskripsi")
            return
        }

        if (TextUtils.isEmpty(activityCreateEditJobBinding.etJobAddress.text.toString().trim())) {
            activityCreateEditJobBinding.etJobAddress.error =
                resources.getString(R.string.error_alert, "Alamat")
            return
        }

        if (TextUtils.isEmpty(activityCreateEditJobBinding.etJobQualification.text.toString().trim())) {
            activityCreateEditJobBinding.etJobQualification.error =
                resources.getString(R.string.error_alert, "Kualifikasi")
            return
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
        val employment = activityCreateEditJobBinding.spJobEmployment.selectedItem.toString().trim()
        val type = activityCreateEditJobBinding.spJobType.selectedItem.toString().trim()
        val industry = activityCreateEditJobBinding.spJobIndustry.selectedItem.toString().trim()

        var salary = resources.getString(R.string.txt_job_salary_value)

        if (!TextUtils.isEmpty(activityCreateEditJobBinding.etJobSalary.text.toString().trim())) {
            salary = activityCreateEditJobBinding.etJobSalary.text.toString().trim()
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate: String = sdf.format(Date())

        val job = JobDetailsResponseEntity(
            id = "",
            title = title,
            description = description,
            address = address,
            qualification = qualification,
            employment = employment,
            type = type,
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