package org.d3ifcool.dissajobrecruiter.ui.job

import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.databinding.ActivityCreateJobBinding
import org.d3ifcool.dissajobrecruiter.databinding.JobPostHeaderBinding
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class CreateJobActivity : AppCompatActivity(), JobPostCallback {

    private lateinit var activityJobPostBinding: ActivityCreateJobBinding
    private lateinit var jobPostHeaderBinding: JobPostHeaderBinding

    private lateinit var viewModel: JobViewModel

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityJobPostBinding = ActivityCreateJobBinding.inflate(layoutInflater)
        setContentView(activityJobPostBinding.root)
        jobPostHeaderBinding = JobPostHeaderBinding.inflate(
            layoutInflater,
            activityJobPostBinding.root.parent as ViewGroup?, true
        )

        spinnerInit()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)

        jobPostHeaderBinding.imgBackBtn.setOnClickListener {
            finish()
        }

        activityJobPostBinding.btnPost.setOnClickListener {
            formValidation()
        }
    }

    private fun spinnerInit() {
        ArrayAdapter.createFromResource(this, R.array.employment, R.layout.spinner_items)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                activityJobPostBinding.spEmployment.adapter = adapter
            }

        ArrayAdapter.createFromResource(this, R.array.industry, R.layout.spinner_items)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                activityJobPostBinding.spIndustry.adapter = adapter
            }
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityJobPostBinding.etTitle.text.toString().trim())) {
            activityJobPostBinding.etTitle.error =
                resources.getString(R.string.error_alert, "Title")
            return
        }

        if (TextUtils.isEmpty(activityJobPostBinding.etDescription.text.toString().trim())) {
            activityJobPostBinding.etDescription.error =
                resources.getString(R.string.error_alert, "Deskripsi")
            return
        }

        if (TextUtils.isEmpty(activityJobPostBinding.etQualification.text.toString().trim())) {
            activityJobPostBinding.etQualification.error =
                resources.getString(R.string.error_alert, "Kualifikasi")
            return
        }

        storeToDatabase()

        dialog.titleText = resources.getString(R.string.loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun storeToDatabase() {

        val title = activityJobPostBinding.etTitle.text.toString()
        val description = activityJobPostBinding.etDescription.text.toString()
        val qualification = activityJobPostBinding.etQualification.text.toString()
        val employment = activityJobPostBinding.spEmployment.selectedItem.toString()
        val industry = activityJobPostBinding.spIndustry.selectedItem.toString()

        var salary = 0

        if (!TextUtils.isEmpty(activityJobPostBinding.etSalary.text.toString().trim())) {
            salary = activityJobPostBinding.etSalary.text.toString().toInt()
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
            postedDate = currentDate,
            updatedDate = currentDate,
            isOpen = true
        )

        viewModel.createJob(job, this)
    }

    override fun onSuccess() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.success_post_job)
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailure(message: String) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(R.string.failure_post_job)
        dialog.setCancelable(false)
        dialog.show()
    }
}