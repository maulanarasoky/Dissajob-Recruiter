package org.d3ifcool.dissajobrecruiter.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_change_password.*
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityChangeEmailBinding
import org.d3ifcool.dissajobrecruiter.databinding.ActivityChangePasswordBinding
import org.d3ifcool.dissajobrecruiter.ui.profile.ProfileViewModel
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.AuthHelper

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener, UpdateProfileCallback {

    private lateinit var activityChangePasswordBinding: ActivityChangePasswordBinding

    private lateinit var viewModel: ProfileViewModel

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(activityChangePasswordBinding.root)

        activityChangePasswordBinding.activityHeader.tvHeaderTitle.text =
            resources.getString(R.string.change_password_title)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        activityChangePasswordBinding.activityHeader.imgBackBtn.setOnClickListener(this)
        activityChangePasswordBinding.btnUpdate.setOnClickListener(this)
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityChangePasswordBinding.etOldPassword.text.toString().trim())) {
            etOldPassword.error = getString(R.string.txt_error_form_text, "Password lama")
            return
        }

        if (TextUtils.isEmpty(activityChangePasswordBinding.etNewPassword.text.toString().trim())) {
            etNewPassword.error = getString(R.string.txt_error_form_text, "Password baru")
            return
        }

        if (TextUtils.isEmpty(
                activityChangePasswordBinding.etConfirmPassword.text.toString().trim()
            )
        ) {
            etConfirmPassword.error = getString(R.string.txt_error_form_text, "Re-type password")
            return
        }

        if (etNewPassword.text.toString() != etConfirmPassword.text.toString()) {
            etNewPassword.error = getString(R.string.password_confirm_password_not_match)
            etConfirmPassword.error = getString(R.string.password_confirm_password_not_match)
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

        viewModel.updatePasswordProfile(email, oldPassword, newPassword, this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBackBtn -> finish()
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

    override fun onFailure(message: String) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = message
        dialog.setCancelable(false)
        dialog.show()
    }
}