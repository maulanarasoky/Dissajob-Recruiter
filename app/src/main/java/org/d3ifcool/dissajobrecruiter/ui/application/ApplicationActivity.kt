package org.d3ifcool.dissajobrecruiter.ui.application

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.ActivityApplicationBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.applicant.LoadApplicantDataCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.OnApplicationClickCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.SendApplicationDataCallback
import org.d3ifcool.dissajobrecruiter.ui.job.JobViewModel
import org.d3ifcool.dissajobrecruiter.ui.job.callback.LoadJobDataCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status
import java.io.File
import java.io.FileOutputStream

class ApplicationActivity : AppCompatActivity(), LoadApplicantDataCallback,
    View.OnClickListener, OnApplicationClickCallback, LoadJobDataCallback,
    SendApplicationDataCallback {

    private lateinit var activityApplicationBinding: ActivityApplicationBinding

    private lateinit var jobViewModel: JobViewModel

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var applicationAdapter: ApplicationAdapter

    private val recruiterId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    private val csv = StringBuilder()

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApplicationBinding = ActivityApplicationBinding.inflate(layoutInflater)
        setContentView(activityApplicationBinding.root)

        activityApplicationBinding.toolbar.title = resources.getString(R.string.applicants)
        setSupportActionBar(activityApplicationBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        csv.append("No,Nama,Email,Nomor Telepon,Tentang,Melamar Sebagai,Tanggal Melamar,Status Lamaran")

        showLoading(true)
        val factory = ViewModelFactory.getInstance(this)
        val applicationViewModel =
            ViewModelProvider(this, factory)[ApplicationViewModel::class.java]
        jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
        applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
        applicationAdapter = ApplicationAdapter(this, this, this, this)
        applicationViewModel.getApplications(recruiterId)
            .observe(this) { applicants ->
                if (applicants != null) {
                    when (applicants.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (applicants.data?.isNotEmpty() == true) {
                                applicationAdapter.submitList(applicants.data)
                                applicationAdapter.notifyDataSetChanged()
                                activityApplicationBinding.tvNoData.visibility = View.GONE
                                activityApplicationBinding.btnExportApplicants.visibility =
                                    View.VISIBLE
                                activityApplicationBinding.btnExportApplicants.setOnClickListener(
                                    this
                                )
                            } else {
                                activityApplicationBinding.tvNoData.visibility = View.VISIBLE
                                activityApplicationBinding.btnExportApplicants.visibility =
                                    View.GONE
                            }
                        }
                        Status.ERROR -> {
                            showLoading(false)
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        with(activityApplicationBinding.rvApplication) {
            layoutManager = LinearLayoutManager(this@ApplicationActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@ApplicationActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = applicationAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activityApplicationBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityApplicationBinding.progressBar.visibility = View.GONE
        }
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
            R.id.imgBackBtn -> finish()
            R.id.btnExportApplicants -> exportApplicationToCsv()
        }
    }

    override fun onLoadApplicantDetailsCallback(
        applicantId: String,
        callback: LoadApplicantDataCallback
    ) {
        applicantViewModel.getApplicantDetails(applicantId).observe(this) { applicantDetails ->
            if (applicantDetails != null) {
                if (applicantDetails.data != null) {
                    callback.onGetApplicantDetails(applicantDetails.data)
                }
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

    private fun exportApplicationToCsv() {
        try {
            val fo: FileOutputStream = openFileOutput("Daftar Pelamar.csv", Context.MODE_PRIVATE)
            fo.write(csv.toString().toByteArray())
            fo.close()

            //exporting
            val context = applicationContext
            val filelocation = File(filesDir, "Daftar Pelamar.csv")
            val path: Uri = FileProvider.getUriForFile(
                context,
                "org.d3ifcool.dissajobrecruiter.fileprovider",
                filelocation
            )
            val fileIntent = Intent(Intent.ACTION_SEND)
            fileIntent.type = "text/csv"
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Daftar Pelamar")
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            fileIntent.putExtra(Intent.EXTRA_STREAM, path)
            startActivity(Intent.createChooser(fileIntent, "Send mail"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}