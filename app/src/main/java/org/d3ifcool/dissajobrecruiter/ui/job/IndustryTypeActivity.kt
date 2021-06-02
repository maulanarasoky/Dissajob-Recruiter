package org.d3ifcool.dissajobrecruiter.ui.job

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityIndustryTypeBinding

class IndustryTypeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_INDUSTRY_TYPE = 300
        const val RESULT_INDUSTRY_TYPE = 301
        const val SELECTED_INDUSTRY_TYPE = "selected_industry_type"
    }

    private lateinit var activityIndustryTypeBinding: ActivityIndustryTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityIndustryTypeBinding = ActivityIndustryTypeBinding.inflate(layoutInflater)
        setContentView(activityIndustryTypeBinding.root)

        activityIndustryTypeBinding.toolbar.title =
            resources.getString(R.string.txt_industry_type)
        setSupportActionBar(activityIndustryTypeBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activityIndustryTypeBinding.tvElectronicType.setOnClickListener(this)
        activityIndustryTypeBinding.tvComputerType.setOnClickListener(this)
        activityIndustryTypeBinding.tvTechnologyType.setOnClickListener(this)
        activityIndustryTypeBinding.tvTelecommunicationType.setOnClickListener(this)
        activityIndustryTypeBinding.tvEntertainmentType.setOnClickListener(this)
        activityIndustryTypeBinding.tvConstructionType.setOnClickListener(this)
        activityIndustryTypeBinding.tvManufactureType.setOnClickListener(this)
        activityIndustryTypeBinding.tvTransportationType.setOnClickListener(this)
        activityIndustryTypeBinding.tvAgricultureType.setOnClickListener(this)
        activityIndustryTypeBinding.tvMiningType.setOnClickListener(this)
        activityIndustryTypeBinding.tvPharmacyType.setOnClickListener(this)
        activityIndustryTypeBinding.tvOtherType.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvElectronicType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Elektronik")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvComputerType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Komputer")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvTechnologyType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Teknologi")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvTelecommunicationType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Telekomunikasi")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvEntertainmentType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Hiburan")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvConstructionType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Konstruksi")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvManufactureType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Manufaktur")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvTransportationType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Transportasi")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvAgricultureType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Pertanian")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvMiningType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Pertambangan")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvPharmacyType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Farmasi")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
            R.id.tvOtherType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_INDUSTRY_TYPE, "Lainnya")
                setResult(RESULT_INDUSTRY_TYPE, intent)
                finish()
            }
        }
    }
}