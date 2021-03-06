package org.d3ifcool.dissajobrecruiter.data.source.repository.user

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback

interface UserDataSource {
    fun createUser(email: String, password: String, user: UserResponseEntity, callback: SignUpCallback)
    fun signIn(email: String, password: String, callback: SignInCallback)
}