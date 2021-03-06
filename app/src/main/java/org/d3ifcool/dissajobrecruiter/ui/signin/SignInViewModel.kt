package org.d3ifcool.dissajobrecruiter.ui.signin

import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.repository.user.UserRepository

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun signIn(
        email: String,
        password: String,
        callback: SignInCallback
    ) = userRepository.signIn(email, password, callback)
}