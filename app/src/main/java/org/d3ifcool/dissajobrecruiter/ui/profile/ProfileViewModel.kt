package org.d3ifcool.dissajobrecruiter.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.UserEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.user.UserRepository
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserProfile(userId: String): LiveData<Resource<UserEntity>> =
        userRepository.getUserProfile(userId)

    fun updateEmailProfile(
        userId: String,
        email: String,
        password: String,
        callback: UpdateProfileCallback
    ) = userRepository.updateEmailProfile(userId, email, password, callback)

    fun updateUserProfile(
        userProfile: UserResponseEntity,
        callback: UpdateProfileCallback
    ) = userRepository.updateProfileData(userProfile, callback)

    fun updatePasswordProfile(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) = userRepository.updatePasswordProfile(email, oldPassword, newPassword, callback)
}