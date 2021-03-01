package org.d3ifcool.dissajobrecruiter.ui.signup

import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    //    private val mUserViewModel: UserViewModel by lazy {
//        val factory = UserViewModelFactory(UserDb.getInstance(), this)
//        ViewModelProvider(this, factory).get(UserViewModel::class.java)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)
//
//        imgBackBtn.setOnClickListener {
//            finish()
//        }
//
//        btnSignUp.setOnClickListener {
//            formValidation()
//        }
//    }
//
//    private fun signUp(email: String, password: String) {
//        val firstName = etFirstName.text.toString()
//        val lastName = etLastName.text.toString()
//        val role = etRole.text.toString()
//        val user = UserEntity(firstName = firstName, lastName = lastName, role = role)
//        mUserViewModel.signUp(email, password, user)
//    }
//
//    private fun formValidation() {
//        if (TextUtils.isEmpty(etFirstName.text.toString().trim())) {
//            etFirstName.error = resources.getString(R.string.error_alert, "Nama depan")
//            return
//        }
//
//        if (TextUtils.isEmpty(etLastName.text.toString().trim())) {
//            etLastName.error = resources.getString(R.string.error_alert, "Nama belakang")
//            return
//        }
//
//        if (TextUtils.isEmpty(etEmail.text.toString().trim())) {
//            etEmail.error = resources.getString(R.string.error_alert, "Email")
//            return
//        }
//
//        if (!isValidMail(etEmail.text.toString().trim())) {
//            etEmail.error = resources.getString(R.string.email_invalid)
//            return
//        }
//
//        if (TextUtils.isEmpty(etPassword.text.toString().trim())) {
//            etPassword.error = resources.getString(R.string.error_alert, "Password")
//            return
//        }
//
//        if (TextUtils.isEmpty(etConfirmPassword.text.toString().trim())) {
//            etConfirmPassword.error = resources.getString(R.string.error_alert, "Confirm password")
//            return
//        }
//
//        if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
//            etPassword.error =
//                resources.getString(R.string.password_doesnt_match, "Password dan confirm password")
//            etConfirmPassword.error =
//                resources.getString(R.string.password_doesnt_match, "Password dan confirm password")
//            return
//        }
//
//        signUp(etEmail.text.toString(), etPassword.text.toString())
//    }
//
//    private fun isValidMail(email: String): Boolean {
//        val emailString = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
//        return Pattern.compile(emailString).matcher(email).matches()
//    }
}