package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant.ApplicantDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobrecruiter.utils.ApplicantHelper
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

    fun getAllApplicants(callback: LoadAllApplicantsCallback): LiveData<ApiResponse<List<ApplicantResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplicant = MutableLiveData<ApiResponse<List<ApplicantResponseEntity>>>()
        applicantHelper.getAllApplicants(object : LoadAllApplicantsCallback {
            override fun onAllApplicantsReceived(applicantsResponse: List<ApplicantResponseEntity>): List<ApplicantResponseEntity> {
                resultApplicant.value =
                    ApiResponse.success(callback.onAllApplicantsReceived(applicantsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicantsResponse
            }
        })
        return resultApplicant
    }

    fun getAcceptedApplicants(callback: LoadAllApplicantsCallback): LiveData<ApiResponse<List<ApplicantResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplicant = MutableLiveData<ApiResponse<List<ApplicantResponseEntity>>>()
        applicantHelper.getAllApplicantsByStatus("accepted", object : LoadAllApplicantsCallback {
            override fun onAllApplicantsReceived(applicantsResponse: List<ApplicantResponseEntity>): List<ApplicantResponseEntity> {
                resultApplicant.value =
                    ApiResponse.success(callback.onAllApplicantsReceived(applicantsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicantsResponse
            }
        })
        return resultApplicant
    }

    fun getRejectedApplicants(callback: LoadAllApplicantsCallback): LiveData<ApiResponse<List<ApplicantResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplicant = MutableLiveData<ApiResponse<List<ApplicantResponseEntity>>>()
        applicantHelper.getAllApplicantsByStatus("rejected", object : LoadAllApplicantsCallback {
            override fun onAllApplicantsReceived(applicantsResponse: List<ApplicantResponseEntity>): List<ApplicantResponseEntity> {
                resultApplicant.value =
                    ApiResponse.success(callback.onAllApplicantsReceived(applicantsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicantsResponse
            }
        })
        return resultApplicant
    }

    fun getMarkedApplicants(callback: LoadAllApplicantsCallback): LiveData<ApiResponse<List<ApplicantResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplicant = MutableLiveData<ApiResponse<List<ApplicantResponseEntity>>>()
        applicantHelper.getMarkedApplicants(object : LoadAllApplicantsCallback {
            override fun onAllApplicantsReceived(applicantsResponse: List<ApplicantResponseEntity>): List<ApplicantResponseEntity> {
                resultApplicant.value =
                    ApiResponse.success(callback.onAllApplicantsReceived(applicantsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicantsResponse
            }
        })
        return resultApplicant
    }

    fun getApplicantsByJob(
        jobId: String,
        callback: LoadAllApplicantsCallback
    ): LiveData<ApiResponse<List<ApplicantResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplicant = MutableLiveData<ApiResponse<List<ApplicantResponseEntity>>>()
        applicantHelper.getApplicantsByJob(jobId, object : LoadAllApplicantsCallback {
            override fun onAllApplicantsReceived(applicantsResponse: List<ApplicantResponseEntity>): List<ApplicantResponseEntity> {
                resultApplicant.value =
                    ApiResponse.success(callback.onAllApplicantsReceived(applicantsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicantsResponse
            }
        })
        return resultApplicant
    }

    fun getApplicantDetails(
        applicantId: String,
        callback: LoadApplicantDetailsCallback
    ): LiveData<ApiResponse<ApplicantDetailsResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultApplicantDetails = MutableLiveData<ApiResponse<ApplicantDetailsResponseEntity>>()
        applicantHelper.getApplicantDetails(applicantId, object : LoadApplicantDetailsCallback {
            override fun onApplicantDetailsReceived(applicantDetailsResponse: ApplicantDetailsResponseEntity): ApplicantDetailsResponseEntity {
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

    interface LoadAllApplicantsCallback {
        fun onAllApplicantsReceived(applicantsResponse: List<ApplicantResponseEntity>): List<ApplicantResponseEntity>

    }

    interface LoadApplicantDetailsCallback {
        fun onApplicantDetailsReceived(applicantDetailsResponse: ApplicantDetailsResponseEntity): ApplicantDetailsResponseEntity
    }

    interface LoadAllApplicantsDetailsCallback {
        fun onAllApplicantDetailsReceived(allApplicantDetailsResponse: List<ApplicantDetailsResponseEntity>): List<ApplicantDetailsResponseEntity>
    }
}