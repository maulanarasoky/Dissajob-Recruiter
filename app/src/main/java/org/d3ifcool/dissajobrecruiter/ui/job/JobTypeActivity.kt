package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityJobTypeBinding

class JobTypeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_JOB_TYPE = 200
        const val RESULT_JOB_TYPE = 201
        const val SELECTED_JOB_TYPE = "selected_job_type"
    }

    private lateinit var activityJobTypeBinding: ActivityJobTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityJobTypeBinding = ActivityJobTypeBinding.inflate(layoutInflater)
        setContentView(activityJobTypeBinding.root)

        activityJobTypeBinding.toolbar.title =
            resources.getString(R.string.txt_job_type)
        setSupportActionBar(activityJobTypeBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activityJobTypeBinding.tvRemoteType.setOnClickListener(this)
        activityJobTypeBinding.tvOnsiteType.setOnClickListener(this)
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
            R.id.tvRemoteType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_JOB_TYPE, "Remote")
                setResult(RESULT_JOB_TYPE, intent)
                finish()
            }
            R.id.tvOnsiteType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_JOB_TYPE, "Onsite")
                setResult(RESULT_JOB_TYPE, intent)
                finish()
            }
        }
    }
}