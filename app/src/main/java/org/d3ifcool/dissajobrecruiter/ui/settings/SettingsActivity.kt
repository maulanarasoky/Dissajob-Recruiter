package org.d3ifcool.dissajobrecruiter.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivitySettingsBinding
import org.d3ifcool.dissajobrecruiter.utils.SignOutDialog

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activitySettingsBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)

        activitySettingsBinding.toolbar.title = resources.getString(R.string.settings)
        setSupportActionBar(activitySettingsBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //Change profile section
        activitySettingsBinding.settingsProfileSection.btnChangeProfile.setOnClickListener(this)
        activitySettingsBinding.settingsProfileSection.btnChangePhoneNumber.setOnClickListener(this)
        activitySettingsBinding.settingsProfileSection.btnChangeEmail.setOnClickListener(this)

        //Change password section
        activitySettingsBinding.settingsSecuritySection.btnChangePassword.setOnClickListener(this)

        //Advance section
        activitySettingsBinding.settingsAdavanceSection.btnSignOut.setOnClickListener(this)
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
            R.id.btnChangeProfile -> startActivity(Intent(this, ChangeProfileActivity::class.java))
            R.id.btnChangePhoneNumber -> startActivity(
                Intent(
                    this,
                    ChangePhoneNumberActivity::class.java
                )
            )
            R.id.btnChangeEmail -> startActivity(Intent(this, ChangeEmailActivity::class.java))
            R.id.btnChangePassword -> startActivity(
                Intent(
                    this,
                    ChangePasswordActivity::class.java
                )
            )
            R.id.btnSignOut -> SignOutDialog().show(
                supportFragmentManager,
                SignOutDialog::class.java.simpleName
            )
        }
    }
}