package org.d3ifcool.dissajobrecruiter.ui.signup

import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.DataRepository
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.UserResponseEntity

class SignUpViewModel(private val dataRepository: DataRepository) : ViewModel() {
    fun signUp(
        email: String,
        password: String,
        user: UserResponseEntity,
        callback: SignUpCallback
    ) = dataRepository.createUser(email, password, user, callback)
}