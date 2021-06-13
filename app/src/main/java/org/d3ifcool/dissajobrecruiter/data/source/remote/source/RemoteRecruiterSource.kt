package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.RecruiterResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.profile.CheckRecruiterDataCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.UploadProfilePictureCallback
import org.d3ifcool.dissajobrecruiter.ui.resetpassword.ResetPasswordCallback
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.database.RecruiterHelper

class RemoteRecruiterSource private constructor(
    private val recruiterHelper: RecruiterHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteRecruiterSource? = null

        fun getInstance(userHelper: RecruiterHelper): RemoteRecruiterSource =
            instance ?: synchronized(this) {
                instance ?: RemoteRecruiterSource(userHelper)
            }
    }

    fun signUp(
        email: String,
        password: String,
        recruiter: RecruiterResponseEntity,
        callback: SignUpCallback
    ) {
        EspressoIdlingResource.increment()
        recruiterHelper.signUp(email, password, recruiter, object : SignUpCallback {
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

    fun getRecruiterData(
        userId: String,
        callback: LoadRecruiterDataCallback
    ): LiveData<ApiResponse<RecruiterResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultUser = MutableLiveData<ApiResponse<RecruiterResponseEntity>>()
        recruiterHelper.getRecruiterData(userId, object : LoadRecruiterDataCallback {
            override fun onRecruiterDataReceived(recruiterResponse: RecruiterResponseEntity): RecruiterResponseEntity {
                resultUser.value =
                    ApiResponse.success(callback.onRecruiterDataReceived(recruiterResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return recruiterResponse
            }
        })
        return resultUser
    }

    fun checkRecruiterData(
        recruiterId: String,
        callback: CheckRecruiterDataCallback
    ) {
        EspressoIdlingResource.increment()
        recruiterHelper.checkRecruiterData(recruiterId, object : CheckRecruiterDataCallback {
            override fun allDataAvailable() {
                callback.allDataAvailable()
                EspressoIdlingResource.decrement()
            }

            override fun profileDataNotAvailable() {
                callback.profileDataNotAvailable()
                EspressoIdlingResource.decrement()
            }

            override fun phoneNumberNotAvailable() {
                callback.phoneNumberNotAvailable()
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun signIn(email: String, password: String, callback: SignInCallback) {
        EspressoIdlingResource.increment()
        recruiterHelper.signIn(email, password, object : SignInCallback {
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

    fun updateProfileData(
        recruiterProfile: RecruiterResponseEntity,
        callback: UpdateProfileCallback
    ) {
        EspressoIdlingResource.increment()
        recruiterHelper.updateRecruiterData(recruiterProfile, object : UpdateProfileCallback {
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

    fun uploadRecruiterProfilePicture(image: Uri, callback: UploadProfilePictureCallback) {
        EspressoIdlingResource.increment()
        recruiterHelper.uploadRecruiterProfilePicture(image, object : UploadProfilePictureCallback {
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

    fun updateRecruiterEmail(
        userId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        EspressoIdlingResource.increment()
        recruiterHelper.updateRecruiterEmail(
            userId,
            newEmail,
            password,
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

    fun updateRecruiterPhoneNumber(
        userId: String,
        newPhoneNumber: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        EspressoIdlingResource.increment()
        recruiterHelper.updateRecruiterPhoneNumber(
            userId,
            newPhoneNumber,
            password,
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

    fun updateRecruiterPassword(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) {
        EspressoIdlingResource.increment()
        recruiterHelper.updateRecruiterPassword(
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

    fun resetPassword(
        email: String,
        callback: ResetPasswordCallback
    ) {
        EspressoIdlingResource.increment()
        recruiterHelper.resetPassword(email, object : ResetPasswordCallback {
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

    interface LoadRecruiterDataCallback {
        fun onRecruiterDataReceived(recruiterResponse: RecruiterResponseEntity): RecruiterResponseEntity
    }
}