package org.d3ifcool.dissajobrecruiter.ui.signin

import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {
    //    private val mUserViewModel: UserViewModel by lazy {
//        val factory = UserViewModelFactory(UserDb.getInstance(), this)
//        ViewModelProvider(this, factory).get(UserViewModel::class.java)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_in)
//
//        if(FirebaseAuth.getInstance().currentUser != null) {
//            startActivity<HomeActivity>()
//            this.finish()
//            return
//        }
//
//        imgBackBtn.setOnClickListener {
//            finish()
//        }
//
//        signInBtn.setOnClickListener {
//            formValidation()
//        }
//
//        signUpBtn.setOnClickListener {
//            startActivity<SignUpActivity>()
//        }
//    }
//
//    private fun checkLogin() {
//
//        val email = etEmail.text.toString()
//        val password = etPassword.text.toString()
//
//        mUserViewModel.signIn(email, password)
//    }
//
//    private fun formValidation() {
//        if (TextUtils.isEmpty(etEmail.text.toString().trim())) {
//            etEmail.error = resources.getString(R.string.error_alert, "Email")
//            return
//        }
//
//        if (TextUtils.isEmpty(etPassword.text.toString().trim())) {
//            etPassword.error = resources.getString(R.string.error_alert, "Password")
//            return
//        }
//
//        checkLogin()
//    }
}