package org.d3ifcool.dissajobrecruiter.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.entity.UserEntity
import org.d3ifcool.dissajobrecruiter.databinding.ActivitySignUpBinding
import org.d3ifcool.dissajobrecruiter.databinding.SignupHeaderBinding
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInActivity
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(), SignUpCallback {

    private lateinit var activitySignUpBinding: ActivitySignUpBinding
    private lateinit var signUpHeaderBinding: SignupHeaderBinding

    private lateinit var viewModel: SignUpViewModel

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(activitySignUpBinding.root)

        signUpHeaderBinding = SignupHeaderBinding.inflate(layoutInflater,
            activitySignUpBinding.root.parent as ViewGroup?, true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[SignUpViewModel::class.java]

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)

        signUpHeaderBinding.imgBackBtn.setOnClickListener {
            finish()
        }

        signUpHeaderBinding.signInBtn.setOnClickListener {
            finish()
        }

        activitySignUpBinding.btnSignUp.setOnClickListener {
            formValidation()
        }
    }

    private fun signUp(email: String, password: String) {
        dialog.titleText = resources.getString(R.string.loading)
        dialog.setCancelable(false)
        dialog.show()


        val firstName = activitySignUpBinding.etFirstName.text.toString().trim()
        val lastName = activitySignUpBinding.etLastName.text.toString().trim()
        val role = activitySignUpBinding.etRole.text.toString().trim()
        val user = UserEntity(firstName = firstName, lastName = lastName, role = role)

        viewModel.signUp(email, password, user, this)
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activitySignUpBinding.etFirstName.text.toString().trim())) {
            activitySignUpBinding.etFirstName.error =
                resources.getString(R.string.error_alert, "Nama depan")
            return
        }

//        if (TextUtils.isEmpty(activitySignUpBinding.etLastName.text.toString().trim())) {
//            activitySignUpBinding.etLastName.error =
//                resources.getString(R.string.error_alert, "Nama belakang")
//            return
//        }

        if (TextUtils.isEmpty(activitySignUpBinding.etEmail.text.toString().trim())) {
            activitySignUpBinding.etEmail.error = resources.getString(R.string.error_alert, "Email")
            return
        }

        if (!isValidMail(activitySignUpBinding.etEmail.text.toString().trim())) {
            activitySignUpBinding.etEmail.error = resources.getString(R.string.email_invalid)
            return
        }

        if (TextUtils.isEmpty(activitySignUpBinding.etPassword.text.toString().trim())) {
            activitySignUpBinding.etPassword.error =
                resources.getString(R.string.error_alert, "Password")
            return
        }

        if (TextUtils.isEmpty(activitySignUpBinding.etConfirmPassword.text.toString().trim())) {
            activitySignUpBinding.etConfirmPassword.error =
                resources.getString(R.string.error_alert, "Confirm password")
            return
        }

        if (activitySignUpBinding.etPassword.text.toString() != activitySignUpBinding.etConfirmPassword.text.toString()) {
            activitySignUpBinding.etPassword.error =
                resources.getString(R.string.password_doesnt_match, "Password dan confirm password")
            activitySignUpBinding.etConfirmPassword.error =
                resources.getString(R.string.password_doesnt_match, "Password dan confirm password")
            return
        }

        signUp(
            activitySignUpBinding.etEmail.text.toString(),
            activitySignUpBinding.etPassword.text.toString()
        )
    }

    private fun isValidMail(email: String): Boolean {
        val emailString = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        return Pattern.compile(emailString).matcher(email).matches()
    }

    override fun onSuccess() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.success_signup)
        dialog.setConfirmClickListener {
            this.finish()
        }
        dialog.show()
    }

    override fun onFailure(message: String) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = message
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }
}