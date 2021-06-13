package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityJobBinding
import org.d3ifcool.dissajobrecruiter.ui.profile.CheckRecruiterDataCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.RecruiterViewModel
import org.d3ifcool.dissajobrecruiter.ui.settings.ChangePhoneNumberActivity
import org.d3ifcool.dissajobrecruiter.ui.settings.ChangeProfileActivity
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.AuthHelper
import org.d3ifcool.dissajobrecruiter.vo.Status

class JobActivity : AppCompatActivity(), JobAdapter.ItemClickListener, View.OnClickListener,
    CheckRecruiterDataCallback {

    private lateinit var activityJobBinding: ActivityJobBinding

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var jobAdapter: JobAdapter

    private var isBtnClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityJobBinding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(activityJobBinding.root)

        activityJobBinding.toolbar.title = resources.getString(R.string.job)
        setSupportActionBar(activityJobBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        showLoading(true)
        val factory = ViewModelFactory.getInstance(this)
        recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
        val jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
        jobAdapter = JobAdapter(this)
        jobViewModel.getJobs().observe(this) { jobs ->
            if (jobs != null) {
                when (jobs.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        if (jobs.data?.isNotEmpty() == true) {
                            jobAdapter.submitList(jobs.data)
                            jobAdapter.notifyDataSetChanged()
                            activityJobBinding.tvNoData.visibility = View.GONE
                        } else {
                            activityJobBinding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showToast(R.string.txt_error_occurred)
                    }
                }
            }
        }

        with(activityJobBinding.rvJob) {
            layoutManager = LinearLayoutManager(this@JobActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@JobActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = jobAdapter
        }

        activityJobBinding.fabAddJob.setOnClickListener(this)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activityJobBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityJobBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(messageId: Int) =
        Toast.makeText(this, resources.getString(messageId), Toast.LENGTH_LONG).show()

    override fun onItemClicked(jobId: String) {
        val intent = Intent(this, JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
        startActivity(intent)
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
            R.id.fabAddJob -> {
                isBtnClicked = true
                recruiterViewModel.checkRecruiterData(
                    AuthHelper.currentUser?.uid.toString(),
                    this
                )
            }
        }
    }

    override fun allDataAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            startActivity(Intent(this, CreateEditJobActivity::class.java))
        }
    }

    override fun profileDataNotAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            showToast(R.string.txt_fill_all_data_alert)
            startActivity(Intent(this, ChangeProfileActivity::class.java))
        }
    }

    override fun phoneNumberNotAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            showToast(R.string.txt_fill_all_data_alert)
            startActivity(Intent(this, ChangePhoneNumberActivity::class.java))
        }
    }
}