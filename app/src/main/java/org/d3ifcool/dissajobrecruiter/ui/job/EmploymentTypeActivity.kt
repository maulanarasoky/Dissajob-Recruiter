package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityEmploymentTypeBinding

class EmploymentTypeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_EMPLOYMENT_TYPE = 100
        const val RESULT_EMPLOYMENT_TYPE = 101
        const val SELECTED_EMPLOYMENT_TYPE = "selected_employment_type"
    }

    private lateinit var activityEmploymentTypeBinding: ActivityEmploymentTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEmploymentTypeBinding = ActivityEmploymentTypeBinding.inflate(layoutInflater)
        setContentView(activityEmploymentTypeBinding.root)

        activityEmploymentTypeBinding.toolbar.title =
            resources.getString(R.string.txt_employment_type)
        setSupportActionBar(activityEmploymentTypeBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activityEmploymentTypeBinding.tvFullTimeType.setOnClickListener(this)
        activityEmploymentTypeBinding.tvPartTimeType.setOnClickListener(this)
        activityEmploymentTypeBinding.tvSelfEmployedType.setOnClickListener(this)
        activityEmploymentTypeBinding.tvFreelanceType.setOnClickListener(this)
        activityEmploymentTypeBinding.tvContractType.setOnClickListener(this)
        activityEmploymentTypeBinding.tvInternshipType.setOnClickListener(this)
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
            R.id.tvFullTimeType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT_TYPE, "Purnawaktu")
                setResult(RESULT_EMPLOYMENT_TYPE, intent)
                finish()
            }
            R.id.tvPartTimeType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT_TYPE, "Paruh waktu")
                setResult(RESULT_EMPLOYMENT_TYPE, intent)
                finish()
            }
            R.id.tvSelfEmployedType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT_TYPE, "Wiraswasta")
                setResult(RESULT_EMPLOYMENT_TYPE, intent)
                finish()
            }
            R.id.tvFreelanceType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT_TYPE, "Pekerja lepas")
                setResult(RESULT_EMPLOYMENT_TYPE, intent)
                finish()
            }
            R.id.tvContractType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT_TYPE, "Kontrak")
                setResult(RESULT_EMPLOYMENT_TYPE, intent)
                finish()
            }
            R.id.tvInternshipType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT_TYPE, "Magang")
                setResult(RESULT_EMPLOYMENT_TYPE, intent)
                finish()
            }
        }
    }
}