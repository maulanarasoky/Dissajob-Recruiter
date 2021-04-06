package org.d3ifcool.dissajobrecruiter.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityChangeEmailBinding
import org.d3ifcool.dissajobrecruiter.ui.profile.ProfileViewModel
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.AuthHelper

class ChangeEmailActivity : AppCompatActivity(), View.OnClickListener, UpdateProfileCallback {

    private lateinit var activityChangeEmailBinding: ActivityChangeEmailBinding

    private lateinit var viewModel: ProfileViewModel

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangeEmailBinding = ActivityChangeEmailBinding.inflate(layoutInflater)
        setContentView(activityChangeEmailBinding.root)

        activityChangeEmailBinding.activityHeader.tvHeaderTitle.text = resources.getString(R.string.change_email_title)

        showCurrentEmail()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        activityChangeEmailBinding.activityHeader.imgBackBtn.setOnClickListener(this)
        activityChangeEmailBinding.btnUpdate.setOnClickListener(this)
    }

    private fun showCurrentEmail() {
        activityChangeEmailBinding.etOldEmail.setText(AuthHelper.currentUser?.email)
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityChangeEmailBinding.etNewEmail.text.toString().trim())) {
            activityChangeEmailBinding.etNewEmail.error = getString(R.string.txt_error_form_text, "Email")
            return
        }
        if (TextUtils.isEmpty(activityChangeEmailBinding.etPassword.text.toString().trim())) {
            activityChangeEmailBinding.etPassword.error = getString(R.string.txt_error_form_text, "Password")
            return
        }
        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun storeToDatabase() {
        val newEmail = activityChangeEmailBinding.etNewEmail.text.toString().trim()
        val password = activityChangeEmailBinding.etPassword.text.toString().trim()
        viewModel.updateEmailProfile(AuthHelper.currentUser?.uid.toString(), newEmail, password, this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.imgBackBtn -> finish()
            R.id.btnUpdate -> {
                formValidation()
            }
        }
    }

    override fun onSuccess() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_success_update_profile, "Email")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailure(message: String) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(R.string.failure_post_job)
        dialog.setCancelable(false)
        dialog.show()
    }
}