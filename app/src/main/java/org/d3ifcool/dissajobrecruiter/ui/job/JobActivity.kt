package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityJobBinding
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class JobActivity : AppCompatActivity(), JobAdapter.ItemClickListener, View.OnClickListener {

    private lateinit var activityJobBinding: ActivityJobBinding

    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityJobBinding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(activityJobBinding.root)

        activityJobBinding.header.tvHeaderTitle.text =
            resources.getString(R.string.job)

        showLoading(true)
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
        jobAdapter = JobAdapter(this)
        viewModel.getJobs().observe(this) { jobs ->
            if (jobs != null) {
                when (jobs.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        if (jobs.data?.isNotEmpty() == true) {
                            jobAdapter.submitList(jobs.data)
                            jobAdapter.notifyDataSetChanged()
                        } else {
                            activityJobBinding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
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

        activityJobBinding.header.imgBackBtn.setOnClickListener(this)
        activityJobBinding.fabAddJob.setOnClickListener(this)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activityJobBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityJobBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onItemClicked(jobId: String) {
        val intent = Intent(this, JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.imgBackBtn -> finish()
            R.id.fabAddJob -> {
                startActivity(Intent(this, CreateEditCreateJobActivity::class.java))
            }
        }
    }
}