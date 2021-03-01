package org.d3ifcool.dissajobrecruiter.ui.signin

import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.DataRepository

class SignInViewModel(private val dataRepository: DataRepository) : ViewModel() {
    fun signIn(
        email: String,
        password: String,
        callback: SignInCallback
    ) = dataRepository.signIn(email, password, callback)
}