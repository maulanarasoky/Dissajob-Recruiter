package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.applicant.LoadApplicantDetailsCallback
import org.d3ifcool.dissajobrecruiter.utils.database.ApplicantHelper
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource

class RemoteApplicantSource private constructor(
    private val applicantHelper: ApplicantHelper
) {
    companion object {
        @Volatile
        private var instance: RemoteApplicantSource? = null

        fun getInstance(applicantHelper: ApplicantHelper): RemoteApplicantSource =
            instance ?: synchronized(this) {
                instance ?: RemoteApplicantSource(applicantHelper)
            }
    }

    fun getApplicantDetails(
        applicantId: String,
        callback: LoadApplicantDetailsCallback
    ): LiveData<ApiResponse<ApplicantResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultApplicantDetails = MutableLiveData<ApiResponse<ApplicantResponseEntity>>()
        applicantHelper.getApplicantDetails(applicantId, object : LoadApplicantDetailsCallback {
            override fun onApplicantDetailsReceived(applicantDetailsResponse: ApplicantResponseEntity): ApplicantResponseEntity {
                resultApplicantDetails.value =
                    ApiResponse.success(callback.onApplicantDetailsReceived(applicantDetailsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicantDetailsResponse
            }
        })
        return resultApplicantDetails
    }
}