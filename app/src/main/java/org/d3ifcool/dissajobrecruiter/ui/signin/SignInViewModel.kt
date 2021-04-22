package org.d3ifcool.dissajobrecruiter.ui.signin

import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.repository.recruiter.RecruiterRepository

class SignInViewModel(private val recruiterRepository: RecruiterRepository) : ViewModel() {
    fun signIn(
        email: String,
        password: String,
        callback: SignInCallback
    ) = recruiterRepository.signIn(email, password, callback)
}