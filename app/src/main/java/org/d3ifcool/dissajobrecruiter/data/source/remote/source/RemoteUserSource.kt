package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.UploadProfilePictureCallback
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

    fun updateProfileData(userProfile: UserResponseEntity, callback: UpdateProfileCallback) {
        EspressoIdlingResource.increment()
        userHelper.updateProfileData(userProfile, object : UpdateProfileCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun uploadProfilePicture(image: Uri, callback: UploadProfilePictureCallback) {
        EspressoIdlingResource.increment()
        userHelper.uploadProfilePicture(image, object : UploadProfilePictureCallback {
            override fun onSuccessUpload(imageId: String) {
                callback.onSuccessUpload(imageId)
                EspressoIdlingResource.decrement()
            }

            override fun onFailureUpload(messageId: Int) {
                callback.onFailureUpload(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun updateEmailProfile(
        userId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        EspressoIdlingResource.increment()
        userHelper.updateEmailProfile(userId, newEmail, password, object : UpdateProfileCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun updatePhoneNumberProfile(
        userId: String,
        newPhoneNumber: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        EspressoIdlingResource.increment()
        userHelper.updatePhoneNumberProfile(userId, newPhoneNumber, password, object : UpdateProfileCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun updatePasswordProfile(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) {
        EspressoIdlingResource.increment()
        userHelper.updatePasswordProfile(
            email,
            oldPassword,
            newPassword,
            object : UpdateProfileCallback {
                override fun onSuccess() {
                    callback.onSuccess()
                    EspressoIdlingResource.decrement()
                }

                override fun onFailure(messageId: Int) {
                    callback.onFailure(messageId)
                    EspressoIdlingResource.decrement()
                }
            })
    }

    interface LoadUserProfileCallback {
        fun onUserProfileReceived(userResponse: UserResponseEntity): UserResponseEntity
    }
}