package org.d3ifcool.dissajobrecruiter.data.source.repository.recruiter

import android.net.Uri
import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalRecruiterSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.RecruiterResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteRecruiterSource
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.UploadProfilePictureCallback
import org.d3ifcool.dissajobrecruiter.ui.resetpassword.ResetPasswordCallback
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.NetworkStateCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class RecruiterRepository private constructor(
    private val remoteRecruiterSource: RemoteRecruiterSource,
    private val localRecruiterSource: LocalRecruiterSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) : RecruiterDataSource {

    companion object {
        @Volatile
        private var instance: RecruiterRepository? = null

        fun getInstance(
            remoteRecruiter: RemoteRecruiterSource,
            localRecruiter: LocalRecruiterSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): RecruiterRepository =
            instance ?: synchronized(this) {
                instance ?: RecruiterRepository(
                    remoteRecruiter,
                    localRecruiter,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun signUp(
        email: String,
        password: String,
        recruiter: RecruiterResponseEntity,
        callback: SignUpCallback
    ) = appExecutors.diskIO()
        .execute { remoteRecruiterSource.signUp(email, password, recruiter, callback) }

    override fun signIn(email: String, password: String, callback: SignInCallback) =
        appExecutors.diskIO()
            .execute { remoteRecruiterSource.signIn(email, password, callback) }

    override fun getRecruiterData(recruiterId: String): LiveData<Resource<RecruiterEntity>> {
        return object :
            NetworkBoundResource<RecruiterEntity, RecruiterResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<RecruiterEntity> =
                localRecruiterSource.getRecruiterData(recruiterId)

            override fun shouldFetch(data: RecruiterEntity?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<RecruiterResponseEntity>> =
                remoteRecruiterSource.getRecruiterData(
                    recruiterId,
                    object : RemoteRecruiterSource.LoadRecruiterDataCallback {
                        override fun onRecruiterDataReceived(recruiterResponse: RecruiterResponseEntity): RecruiterResponseEntity {
                            return recruiterResponse
                        }
                    })

            public override fun saveCallResult(data: RecruiterResponseEntity) {
                val userProfile = RecruiterEntity(
                    data.id.toString(),
                    data.firstName,
                    data.lastName,
                    data.fullName,
                    data.email,
                    data.phoneNumber,
                    data.address,
                    data.aboutMe,
                    data.imagePath,
                )
                localRecruiterSource.insertRecruiterData(userProfile)
            }
        }.asLiveData()
    }

    override fun uploadRecruiterProfilePicture(image: Uri, callback: UploadProfilePictureCallback) =
        appExecutors.diskIO()
            .execute { remoteRecruiterSource.uploadRecruiterProfilePicture(image, callback) }

    override fun updateRecruiterData(
        recruiterProfile: RecruiterResponseEntity,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO()
        .execute { remoteRecruiterSource.updateProfileData(recruiterProfile, callback) }

    override fun updateRecruiterEmail(
        recruiterId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO()
        .execute {
            remoteRecruiterSource.updateRecruiterEmail(
                recruiterId,
                newEmail,
                password,
                callback
            )
        }

    override fun updateRecruiterPhoneNumber(
        recruiterId: String,
        newPhoneNumber: String,
        password: String,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO()
        .execute {
            remoteRecruiterSource.updateRecruiterPhoneNumber(
                recruiterId,
                newPhoneNumber,
                password,
                callback
            )
        }

    override fun updateRecruiterPassword(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO()
        .execute {
            remoteRecruiterSource.updateRecruiterPassword(
                email,
                oldPassword,
                newPassword,
                callback
            )
        }

    override fun resetPassword(email: String, callback: ResetPasswordCallback) =
        appExecutors.diskIO()
            .execute {
                remoteRecruiterSource.resetPassword(
                    email,
                    callback
                )
            }
}