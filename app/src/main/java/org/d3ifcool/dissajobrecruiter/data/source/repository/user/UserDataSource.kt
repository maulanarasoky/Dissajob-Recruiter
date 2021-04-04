package org.d3ifcool.dissajobrecruiter.data.source.repository.user

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.UserEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface UserDataSource {
    fun createUser(email: String, password: String, user: UserResponseEntity, callback: SignUpCallback)
    fun signIn(email: String, password: String, callback: SignInCallback)
    fun getUserProfile(userId: String): LiveData<Resource<UserEntity>>
    fun updateProfileData(userProfile: UserResponseEntity, callback: UpdateProfileCallback)
    fun updateEmailProfile(userId: String, email: String, password: String, callback: UpdateProfileCallback)
}