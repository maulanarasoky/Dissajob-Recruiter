package org.d3ifcool.dissajobrecruiter.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.RecruiterResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.recruiter.RecruiterRepository
import org.d3ifcool.dissajobrecruiter.ui.resetpassword.ResetPasswordCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class RecruiterViewModel(private val recruiterRepository: RecruiterRepository) : ViewModel() {
    fun getRecruiterData(userId: String): LiveData<Resource<RecruiterEntity>> =
        recruiterRepository.getRecruiterData(userId)

    fun updateRecruiterData(
        recruiterProfile: RecruiterResponseEntity,
        callback: UpdateProfileCallback
    ) = recruiterRepository.updateRecruiterData(recruiterProfile, callback)

    fun uploadRecruiterProfilePicture(
        image: Uri,
        callback: UploadProfilePictureCallback
    ) = recruiterRepository.uploadRecruiterProfilePicture(image, callback)

    fun updateRecruiterEmail(
        userId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) = recruiterRepository.updateRecruiterEmail(userId, newEmail, password, callback)

    fun updateRecruiterPhoneNumber(
        userId: String,
        newPhoneNumber: String,
        password: String,
        callback: UpdateProfileCallback
    ) = recruiterRepository.updateRecruiterPhoneNumber(userId, newPhoneNumber, password, callback)

    fun updateRecruiterPassword(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) = recruiterRepository.updateRecruiterPassword(email, oldPassword, newPassword, callback)

    fun resetPassword(
        email: String,
        callback: ResetPasswordCallback
    ) = recruiterRepository.resetPassword(email, callback)
}