package org.d3ifcool.dissajobrecruiter.data.source.repository.recruiter

import android.net.Uri
import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.RecruiterResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.UploadProfilePictureCallback
import org.d3ifcool.dissajobrecruiter.ui.resetpassword.ResetPasswordCallback
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface RecruiterDataSource {
    fun signUp(email: String, password: String, recruiter: RecruiterResponseEntity, callback: SignUpCallback)
    fun signIn(email: String, password: String, callback: SignInCallback)
    fun getRecruiterData(recruiterId: String): LiveData<Resource<RecruiterEntity>>
    fun uploadRecruiterProfilePicture(image: Uri, callback: UploadProfilePictureCallback)
    fun updateRecruiterData(recruiterProfile: RecruiterResponseEntity, callback: UpdateProfileCallback)
    fun updateRecruiterEmail(recruiterId: String, newEmail: String, password: String, callback: UpdateProfileCallback)
    fun updateRecruiterPhoneNumber(recruiterId: String, newPhoneNumber: String, password: String, callback: UpdateProfileCallback)
    fun updateRecruiterPassword(email: String, oldPassword: String, newPassword: String, callback: UpdateProfileCallback)
    fun resetPassword(email: String, callback: ResetPasswordCallback)
}