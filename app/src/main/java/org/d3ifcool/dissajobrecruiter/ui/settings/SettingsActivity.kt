package org.d3ifcool.dissajobrecruiter.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activitySettingsBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)

        activitySettingsBinding.header.tvHeaderTitle.text = resources.getString(R.string.settings)
        activitySettingsBinding.header.imgBackBtn.setOnClickListener(this)

        //Change profile section
        activitySettingsBinding.profileSection.btnChangeProfile.setOnClickListener(this)
        activitySettingsBinding.profileSection.btnChangePhoneNumber.setOnClickListener(this)
        activitySettingsBinding.profileSection.btnChangeEmail.setOnClickListener(this)

        //Change password section
        activitySettingsBinding.securitySection.btnChangePassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBackBtn -> finish()
            R.id.btnChangeProfile -> {
                startActivity(Intent(this, ChangeProfileActivity::class.java))
            }
            R.id.btnChangePhoneNumber -> {
                startActivity(Intent(this, ChangePhoneNumberActivity::class.java))
            }
            R.id.btnChangeEmail -> {
                startActivity(Intent(this, ChangeEmailActivity::class.java))
            }
            R.id.btnChangePassword -> {
                startActivity(Intent(this, ChangePasswordActivity::class.java))
            }
        }
    }
}