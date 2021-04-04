package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.UserEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
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

    fun getUserProfile(
        userId: String,
        callback: LoadUserProfileCallback
    ): LiveData<ApiResponse<UserResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultUser = MutableLiveData<ApiResponse<UserResponseEntity>>()
        userHelper.getUserProfile(userId, object : LoadUserProfileCallback {
            override fun onUserProfileReceived(userResponse: UserResponseEntity): UserResponseEntity {
                resultUser.value = ApiResponse.success(callback.onUserProfileReceived(userResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return userResponse
            }
        })
        return resultUser
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

    fun updateEmailProfile(userId: String, email: String, callback: UpdateProfileCallback) {
        EspressoIdlingResource.increment()
        userHelper.updateEmailProfile(userId, email, object : UpdateProfileCallback {
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

    interface LoadUserProfileCallback {
        fun onUserProfileReceived(userResponse: UserResponseEntity): UserResponseEntity
    }
}