package org.d3ifcool.dissajobrecruiter.ui.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityChangeEmailBinding
import org.d3ifcool.dissajobrecruiter.ui.profile.RecruiterViewModel
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import java.util.regex.Pattern

class ChangeEmailActivity : AppCompatActivity(), View.OnClickListener, UpdateProfileCallback {

    private lateinit var activityChangeEmailBinding: ActivityChangeEmailBinding

    private lateinit var viewModel: RecruiterViewModel

    private lateinit var dialog: SweetAlertDialog

    private val recruiterId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    private val recruiterEmail: String = FirebaseAuth.getInstance().currentUser?.email.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangeEmailBinding = ActivityChangeEmailBinding.inflate(layoutInflater)
        setContentView(activityChangeEmailBinding.root)

        activityChangeEmailBinding.toolbar.title = resources.getString(R.string.change_email_title)
        setSupportActionBar(activityChangeEmailBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        showCurrentEmail()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]

        activityChangeEmailBinding.btnUpdate.setOnClickListener(this)
    }

    private fun showCurrentEmail() {
        activityChangeEmailBinding.etOldEmail.setText(recruiterEmail)
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityChangeEmailBinding.etNewEmail.text.toString().trim())) {
            activityChangeEmailBinding.etNewEmail.error =
                getString(R.string.txt_error_form_text, "Email")
            return
        }
        if (TextUtils.isEmpty(activityChangeEmailBinding.etPassword.text.toString().trim())) {
            activityChangeEmailBinding.etPassword.error =
                getString(R.string.txt_error_form_text, "Password")
            return
        }

        storeToDatabase()
    }

    private fun storeToDatabase() {
        val newEmail = activityChangeEmailBinding.etNewEmail.text.toString().trim()
        if (!isValidMail(newEmail)) {
            Toast.makeText(this, resources.getString(R.string.email_invalid), Toast.LENGTH_SHORT)
                .show()
            return
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.loading)
        dialog.setCancelable(false)
        dialog.show()

        val password = activityChangeEmailBinding.etPassword.text.toString().trim()
        viewModel.updateRecruiterEmail(
            recruiterId,
            newEmail,
            password,
            this
        )
    }

    private fun isValidMail(email: String): Boolean {
        val emailString = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        return Pattern.compile(emailString).matcher(email).matches()
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
        dialog.titleText = resources.getString(R.string.txt_success_update_profile, "Email")
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