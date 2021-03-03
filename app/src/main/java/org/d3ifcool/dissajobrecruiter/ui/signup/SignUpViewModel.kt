package org.d3ifcool.dissajobrecruiter.ui.signup

import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.entity.UserEntity
import org.d3ifcool.dissajobrecruiter.data.source.DataRepository

class SignUpViewModel(private val dataRepository: DataRepository) : ViewModel() {
    fun signUp(
        email: String,
        password: String,
        user: UserEntity,
        callback: SignUpCallback
    ) = dataRepository.createUser(email, password, user, callback)
}