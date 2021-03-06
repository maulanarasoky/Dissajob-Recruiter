package org.d3ifcool.dissajobrecruiter.ui.signup

import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.repository.user.UserRepository
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun signUp(
        email: String,
        password: String,
        user: UserResponseEntity,
        callback: SignUpCallback
    ) = userRepository.createUser(email, password, user, callback)
}