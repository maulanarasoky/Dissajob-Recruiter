package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.UserHelper

class RemoteUserSource private constructor(
    private val userHelper: UserHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteUserSource? = null

        fun getInstance(userHelper: UserHelper): RemoteUserSource =
            instance ?: synchronized(this) {
                instance ?: RemoteUserSource(userHelper)
            }
    }

    fun createUser(
        email: String,
        password: String,
        user: UserResponseEntity,
        callback: SignUpCallback
    ) {
        EspressoIdlingResource.increment()
        userHelper.signUp(email, password, user, object : SignUpCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun signIn(email: String, password: String, callback: SignInCallback) {
        EspressoIdlingResource.increment()
        userHelper.signIn(email, password, object : SignInCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onNotVerified() {
                callback.onNotVerified()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure() {
                callback.onFailure()
                EspressoIdlingResource.decrement()
            }
        })
    }
}