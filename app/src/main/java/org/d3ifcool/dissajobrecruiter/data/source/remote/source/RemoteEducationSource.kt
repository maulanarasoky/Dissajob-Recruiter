package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.applicant.education.LoadEducationsCallback
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.database.EducationHelper

class RemoteEducationSource private constructor(
    private val educationHelper: EducationHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteEducationSource? = null

        fun getInstance(educationHelper: EducationHelper): RemoteEducationSource =
            instance ?: synchronized(this) {
                instance ?: RemoteEducationSource(educationHelper)
            }
    }

    fun getApplicantEducations(
        applicantId: String,
        callback: LoadEducationsCallback
    ): LiveData<ApiResponse<List<EducationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultEducation = MutableLiveData<ApiResponse<List<EducationResponseEntity>>>()
        educationHelper.getApplicantEducations(applicantId, object : LoadEducationsCallback {
            override fun onAllEducationsReceived(educationResponse: List<EducationResponseEntity>): List<EducationResponseEntity> {
                resultEducation.value =
                    ApiResponse.success(callback.onAllEducationsReceived(educationResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return educationResponse
            }
        })
        return resultEducation
    }
}