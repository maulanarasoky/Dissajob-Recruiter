package org.d3ifcool.dissajobrecruiter.ui.resetpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityResetPasswordBinding
import org.d3ifcool.dissajobrecruiter.ui.profile.RecruiterViewModel
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import java.util.regex.Pattern

class ResetPasswordActivity : AppCompatActivity(), ResetPasswordCallback, View.OnClickListener {

    private lateinit var activityResetPasswordBinding: ActivityResetPasswordBinding

    private lateinit var viewModel: RecruiterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResetPasswordBinding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(activityResetPasswordBinding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]

        activityResetPasswordBinding.imgBackBtn.setOnClickListener(this)
        activityResetPasswordBinding.btnResetPassword.setOnClickListener(this)
    }

    private fun formValidation() {
        val email = activityResetPasswordBinding.etEmail.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            etEmail.error = resources.getString(R.string.txt_error_form_text, "Email")
            return
        }

        if (!isValidMail(email)) {
            etEmail.error = resources.getString(R.string.email_invalid)
            return
        }

        viewModel.resetPassword(email, this)
    }

    private fun isValidMail(email: String): Boolean {
        val emailString = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        return Pattern.compile(emailString).matcher(email).matches()
    }

    override fun onSuccess() {
        Toast.makeText(
            this,
            resources.getString(R.string.success_alert_reset_password),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    override fun onFailure(messageId: Int) {
        Toast.makeText(this, resources.getString(messageId), Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBackBtn -> finish()
            R.id.btnResetPassword -> formValidation()
        }
    }
}