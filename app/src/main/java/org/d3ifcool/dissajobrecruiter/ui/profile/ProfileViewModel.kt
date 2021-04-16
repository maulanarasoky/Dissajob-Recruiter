package org.d3ifcool.dissajobrecruiter.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.UserEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.user.UserRepository
import org.d3ifcool.dissajobrecruiter.ui.resetpassword.ResetPasswordCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserProfile(userId: String): LiveData<Resource<UserEntity>> =
        userRepository.getUserProfile(userId)

    fun updateUserProfile(
        userProfile: UserResponseEntity,
        callback: UpdateProfileCallback
    ) = userRepository.updateProfileData(userProfile, callback)

    fun uploadProfilePicture(
        image: Uri,
        callback: UploadProfilePictureCallback
    ) = userRepository.uploadProfilePicture(image, callback)

    fun updateEmailProfile(
        userId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) = userRepository.updateEmailProfile(userId, newEmail, password, callback)

    fun updatePhoneNumberProfile(
        userId: String,
        newPhoneNumber: String,
        password: String,
        callback: UpdateProfileCallback
    ) = userRepository.updatePhoneNumberProfile(userId, newPhoneNumber, password, callback)

    fun updatePasswordProfile(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) = userRepository.updatePasswordProfile(email, oldPassword, newPassword, callback)

    fun resetPassword(
        email: String,
        callback: ResetPasswordCallback
    ) = userRepository.resetPassword(email, callback)
}