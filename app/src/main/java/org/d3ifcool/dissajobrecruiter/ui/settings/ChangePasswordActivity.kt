package org.d3ifcool.dissajobrecruiter.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityChangePasswordBinding
import org.d3ifcool.dissajobrecruiter.ui.profile.RecruiterViewModel
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.AuthHelper

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener, UpdateProfileCallback {

    private lateinit var activityChangePasswordBinding: ActivityChangePasswordBinding

    private lateinit var viewModel: RecruiterViewModel

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(activityChangePasswordBinding.root)

        activityChangePasswordBinding.toolbar.title = resources.getString(R.string.change_password_title)
        setSupportActionBar(activityChangePasswordBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]

        activityChangePasswordBinding.btnUpdate.setOnClickListener(this)
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityChangePasswordBinding.etOldPassword.text.toString().trim())) {
            activityChangePasswordBinding.etOldPassword.error = getString(R.string.txt_error_form_text, "Password lama")
            return
        }

        if (TextUtils.isEmpty(activityChangePasswordBinding.etNewPassword.text.toString().trim())) {
            activityChangePasswordBinding.etNewPassword.error = getString(R.string.txt_error_form_text, "Password baru")
            return
        }

        if (TextUtils.isEmpty(
                activityChangePasswordBinding.etConfirmPassword.text.toString().trim()
            )
        ) {
            activityChangePasswordBinding.etConfirmPassword.error = getString(R.string.txt_error_form_text, "Re-type password")
            return
        }

        if (activityChangePasswordBinding.etNewPassword.text.toString() != activityChangePasswordBinding.etConfirmPassword.text.toString()) {
            activityChangePasswordBinding.etNewPassword.error = getString(R.string.password_confirm_password_not_match)
            activityChangePasswordBinding.etConfirmPassword.error = getString(R.string.password_confirm_password_not_match)
            return
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.loading)
        dialog.setCancelable(false)
        dialog.show()

        updatePassword()
    }

    private fun updatePassword() {
        val email = AuthHelper.currentUser?.email.toString()
        val oldPassword = activityChangePasswordBinding.etOldPassword.text.toString().trim()
        val newPassword = activityChangePasswordBinding.etNewPassword.text.toString().trim()

        viewModel.updateRecruiterPassword(email, oldPassword, newPassword, this)
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
            R.id.btnUpdate -> {
                formValidation()
            }
        }
    }

    override fun onSuccess() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_success_update_profile, "Password")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailure(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId)
        dialog.setCancelable(false)
        dialog.show()
    }
}