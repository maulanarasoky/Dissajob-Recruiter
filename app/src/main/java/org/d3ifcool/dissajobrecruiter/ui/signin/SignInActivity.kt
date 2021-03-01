package org.d3ifcool.dissajobrecruiter.ui.signin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivitySignInBinding
import org.d3ifcool.dissajobrecruiter.databinding.SigninHeaderBinding
import org.d3ifcool.dissajobrecruiter.ui.home.HomeActivity
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpActivity
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory

class SignInActivity : AppCompatActivity(), SignInCallback {

    private lateinit var activitySignInBinding: ActivitySignInBinding
    private lateinit var signInHeaderBinding: SigninHeaderBinding

    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(activitySignInBinding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[SignInViewModel::class.java]

        signInHeaderBinding.imgBackBtn.setOnClickListener {
            finish()
        }

        activitySignInBinding.signInBtn.setOnClickListener {
            formValidation()
        }

        signInHeaderBinding.signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun checkLogin() {

        val email = activitySignInBinding.etEmail.text.toString().trim()
        val password = activitySignInBinding.etPassword.text.toString().trim()

        viewModel.signIn(email, password, this)
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activitySignInBinding.etEmail.text.toString().trim())) {
            activitySignInBinding.etEmail.error = resources.getString(R.string.error_alert, "Email")
            return
        }

        if (TextUtils.isEmpty(activitySignInBinding.etPassword.text.toString().trim())) {
            activitySignInBinding.etPassword.error =
                resources.getString(R.string.error_alert, "Password")
            return
        }

        checkLogin()
    }

    override fun onSuccess() {
        Intent(this, HomeActivity::class.java)
        this.finish()
    }

    override fun onNotVerified() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(
            this,
            resources.getString(R.string.email_is_not_verified),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onFailure() {
        Toast.makeText(this, resources.getString(R.string.error_login), Toast.LENGTH_SHORT).show()
    }
}