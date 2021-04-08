package org.d3ifcool.dissajobrecruiter.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityChangeEmailBinding
import org.d3ifcool.dissajobrecruiter.databinding.ActivityChangePhoneNumberBinding
import org.d3ifcool.dissajobrecruiter.ui.profile.ProfileViewModel
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.AuthHelper
import org.d3ifcool.dissajobrecruiter.vo.Status
import java.util.regex.Pattern

class ChangePhoneNumberActivity : AppCompatActivity(), View.OnClickListener, UpdateProfileCallback {

    private lateinit var activityChangePhoneNumberBinding: ActivityChangePhoneNumberBinding

    private lateinit var viewModel: ProfileViewModel

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_phone_number)

        activityChangePhoneNumberBinding.activityHeader.tvHeaderTitle.text = resources.getString(R.string.change_email_title)

        showCurrentPhoneNumber()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        activityChangePhoneNumberBinding.activityHeader.imgBackBtn.setOnClickListener(this)
        activityChangePhoneNumberBinding.btnUpdate.setOnClickListener(this)
    }

    private fun showCurrentPhoneNumber() {
        viewModel.getUserProfile(AuthHelper.currentUser?.uid.toString()).observe(this) { profileData ->
            if (profileData.data != null) {
                when (profileData.status) {
                    Status.LOADING -> {}
                    Status.SUCCESS -> activityChangePhoneNumberBinding.etOldPhoneNumber.setText(profileData.data.phoneNumber)
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityChangePhoneNumberBinding.etNewPhoneNumber.text.toString().trim())) {
            activityChangePhoneNumberBinding.etNewPhoneNumber.error = getString(R.string.txt_error_form_text, "Nomor baru")
            return
        }
        if (TextUtils.isEmpty(activityChangePhoneNumberBinding.etPassword.text.toString().trim())) {
            activityChangePhoneNumberBinding.etPassword.error = getString(R.string.txt_error_form_text, "Password")
            return
        }

        if (activityChangePhoneNumberBinding.etOldPhoneNumber.visibility == View.VISIBLE) {
            if (activityChangePhoneNumberBinding.etOldPhoneNumber.text.toString().trim() == activityChangePhoneNumberBinding.etNewPhoneNumber.text.toString()
                    .trim()
            ) {
                Toast.makeText(this, resources.getString(R.string.txt_success_update_profile, "Nomor telepon"), Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.loading)
        dialog.setCancelable(false)
        dialog.show()

        storeToDatabase()
    }

    private fun storeToDatabase() {
        val newPhoneNumber = activityChangePhoneNumberBinding.etNewPhoneNumber.text.toString().trim()
        if (!isValidMobile(newPhoneNumber)) {
            Toast.makeText(this, resources.getString(R.string.phone_number_invalid), Toast.LENGTH_SHORT).show()
            return
        }

        val password = activityChangePhoneNumberBinding.etPassword.text.toString().trim()
        viewModel.updatePhoneNumberProfile(AuthHelper.currentUser?.uid.toString(), newPhoneNumber, password, this)
    }

    private fun isValidMobile(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length in 7..13
        } else false
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