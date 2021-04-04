package org.d3ifcool.dissajobrecruiter.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.*
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.DateUtils
import org.d3ifcool.dissajobrecruiter.vo.Status

class JobDetailsActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var activityJobDetailsBinding: ActivityJobDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityJobDetailsBinding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(activityJobDetailsBinding.root)

        activityJobDetailsBinding.jobDetailsHeader.imgBackBtn.setOnClickListener(this)

        val extras = intent.extras
        if (extras != null) {
            val factory = ViewModelFactory.getInstance(this)
            val viewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            val jobId = extras.getString(EXTRA_ID)
            if (jobId != null) {
                viewModel.getJobDetails(jobId).observe(this) { jobDetails ->
                    when (jobDetails.status) {
                        Status.LOADING -> {
                        }

                        Status.SUCCESS -> jobDetails.data?.let {
                            populateData(it)
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
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
        activityJobDetailsBinding.jobDetailsTitleSection.tvJobPostedDateAndApplicants.text =
            resources.getString(R.string.job_details_date_format, postedDate, 0)

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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBackBtn -> finish()
        }
    }
}