package org.d3ifcool.dissajobrecruiter.data.source

import org.d3ifcool.dissajobrecruiter.data.entity.UserEntity
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback

interface UserDataSource {
    fun createUser(email: String, password: String, user: UserEntity, callback: SignUpCallback)
    fun signIn(email: String, password: String, callback: SignInCallback)
}