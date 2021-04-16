package org.d3ifcool.dissajobrecruiter.ui.applicant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.ActivityApplicantBinding
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class ApplicantActivity : AppCompatActivity(), ApplicantAdapter.LoadApplicantDetailsCallback,
    View.OnClickListener {

    private lateinit var activityApplicantBinding: ActivityApplicantBinding

    private lateinit var viewModel: ApplicantViewModel

    private lateinit var applicantAdapter: ApplicantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApplicantBinding = ActivityApplicantBinding.inflate(layoutInflater)
        setContentView(activityApplicantBinding.root)

        activityApplicantBinding.toolbar.title = resources.getString(R.string.applicants)
        setSupportActionBar(activityApplicantBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        showLoading(true)
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
        applicantAdapter = ApplicantAdapter(this)
        viewModel.getApplicants().observe(this) { applicants ->
            if (applicants != null) {
                when (applicants.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        if (applicants.data?.isNotEmpty() == true) {
                            applicantAdapter.submitList(applicants.data)
                            applicantAdapter.notifyDataSetChanged()
                        } else {
                            activityApplicantBinding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        with(activityApplicantBinding.rvApplicant) {
            layoutManager = LinearLayoutManager(this@ApplicantActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@ApplicantActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = applicantAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activityApplicantBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityApplicantBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onLoadApplicantDetailsCallback(
        applicantId: String,
        callback: ApplicantAdapter.LoadApplicantDetailsCallback
    ) {
        viewModel.getApplicantDetails(applicantId).observe(this) { applicantDetails ->
            if (applicantDetails != null) {
                callback.onGetApplicantDetails(applicantDetails.data!!)
            }
        }
    }

    override fun onGetApplicantDetails(applicantDetails: ApplicantDetailsEntity) {
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
        }
    }
}