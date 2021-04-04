package org.d3ifcool.dissajobrecruiter.data.source.repository.user

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.UserEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalUserSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteUserSource
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.NetworkStateCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class UserRepository private constructor(
    private val remoteUserSource: RemoteUserSource,
    private val localUserSource: LocalUserSource,
    private val appExecutors: AppExecutors, private val networkCallback: NetworkStateCallback
) : UserDataSource {

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            remoteUser: RemoteUserSource,
            localUser: LocalUserSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(remoteUser, localUser, appExecutors, networkCallback)
            }
    }

    override fun createUser(
        email: String,
        password: String,
        user: UserResponseEntity,
        callback: SignUpCallback
    ) = appExecutors.diskIO()
        .execute { remoteUserSource.createUser(email, password, user, callback) }

    override fun signIn(email: String, password: String, callback: SignInCallback) =
        appExecutors.diskIO()
            .execute { remoteUserSource.signIn(email, password, callback) }

    override fun getUserProfile(userId: String): LiveData<Resource<UserEntity>> {
        return object :
            NetworkBoundResource<UserEntity, UserResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<UserEntity> =
                localUserSource.getUserProfile(userId)

            override fun shouldFetch(data: UserEntity?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<UserResponseEntity>> =
                remoteUserSource.getUserProfile(
                    userId,
                    object : RemoteUserSource.LoadUserProfileCallback {
                        override fun onUserProfileReceived(userResponse: UserResponseEntity): UserResponseEntity {
                            return userResponse
                        }
                    })

            public override fun saveCallResult(data: UserResponseEntity) {
                val userProfile = UserEntity(
                    data.id.toString(),
                    data.firstName,
                    data.lastName,
                    data.fullName,
                    data.email,
                    data.address,
                    data.phoneNumber,
                    data.role,
                    data.imagePath,
                )
                localUserSource.insertUserProfile(userProfile)
            }
        }.asLiveData()
    }

    override fun updateProfileData(
        userProfile: UserResponseEntity,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO().execute { remoteUserSource.updateProfileData(userProfile, callback) }

    override fun updateEmailProfile(
        userId: String,
        email: String,
        password: String,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO()
        .execute { remoteUserSource.updateEmailProfile(userId, email, password, callback) }

    override fun updatePasswordProfile(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO()
        .execute {
            remoteUserSource.updatePasswordProfile(
                email,
                oldPassword,
                newPassword,
                callback
            )
        }
}