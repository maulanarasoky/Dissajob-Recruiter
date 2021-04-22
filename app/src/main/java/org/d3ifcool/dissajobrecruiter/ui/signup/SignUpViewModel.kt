package org.d3ifcool.dissajobrecruiter.ui.signup

import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.repository.recruiter.RecruiterRepository
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.RecruiterResponseEntity

class SignUpViewModel(private val recruiterRepository: RecruiterRepository) : ViewModel() {
    fun signUp(
        email: String,
        password: String,
        recruiter: RecruiterResponseEntity,
        callback: SignUpCallback
    ) = recruiterRepository.signUp(email, password, recruiter, callback)
}