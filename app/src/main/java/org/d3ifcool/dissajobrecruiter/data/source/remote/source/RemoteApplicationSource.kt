package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.application.callback.*
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.database.ApplicationHelper

class RemoteApplicationSource private constructor(
    private val applicationHelper: ApplicationHelper
) {
    companion object {
        @Volatile
        private var instance: RemoteApplicationSource? = null

        fun getInstance(applicationHelper: ApplicationHelper): RemoteApplicationSource =
            instance ?: synchronized(this) {
                instance ?: RemoteApplicationSource(applicationHelper)
            }
    }

    fun getAllApplications(
        recruiterId: String,
        callback: LoadAllApplicationsCallback
    ): LiveData<ApiResponse<List<ApplicationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplication = MutableLiveData<ApiResponse<List<ApplicationResponseEntity>>>()
        applicationHelper.getAllApplications(recruiterId, object : LoadAllApplicationsCallback {
            override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                resultApplication.value =
                    ApiResponse.success(callback.onAllApplicationsReceived(applicationsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicationsResponse
            }
        })
        return resultApplication
    }

    fun getApplicationById(
        applicationId: String,
        callback: LoadApplicationDataCallback
    ): LiveData<ApiResponse<ApplicationResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultApplication = MutableLiveData<ApiResponse<ApplicationResponseEntity>>()
        applicationHelper.getApplicationById(applicationId, object : LoadApplicationDataCallback {
            override fun onApplicationDataReceived(applicationResponse: ApplicationResponseEntity): ApplicationResponseEntity {
                resultApplication.value =
                    ApiResponse.success(callback.onApplicationDataReceived(applicationResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicationResponse
            }
        })
        return resultApplication
    }

    fun getAcceptedApplications(
        recruiterId: String,
        callback: LoadAllApplicationsCallback
    ): LiveData<ApiResponse<List<ApplicationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplication = MutableLiveData<ApiResponse<List<ApplicationResponseEntity>>>()
        applicationHelper.getAllApplicationsByStatus(recruiterId, "Accepted", object :
            LoadAllApplicationsCallback {
            override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                resultApplication.value =
                    ApiResponse.success(callback.onAllApplicationsReceived(applicationsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicationsResponse
            }
        })
        return resultApplication
    }

    fun getRejectedApplications(
        recruiterId: String,
        callback: LoadAllApplicationsCallback
    ): LiveData<ApiResponse<List<ApplicationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplication = MutableLiveData<ApiResponse<List<ApplicationResponseEntity>>>()
        applicationHelper.getAllApplicationsByStatus(recruiterId, "Rejected", object :
            LoadAllApplicationsCallback {
            override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                resultApplication.value =
                    ApiResponse.success(callback.onAllApplicationsReceived(applicationsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicationsResponse
            }
        })
        return resultApplication
    }

    fun getMarkedApplications(
        recruiterId: String,
        callback: LoadAllApplicationsCallback
    ): LiveData<ApiResponse<List<ApplicationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplication = MutableLiveData<ApiResponse<List<ApplicationResponseEntity>>>()
        applicationHelper.getMarkedApplications(recruiterId, object : LoadAllApplicationsCallback {
            override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                resultApplication.value =
                    ApiResponse.success(callback.onAllApplicationsReceived(applicationsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicationsResponse
            }
        })
        return resultApplication
    }

    fun getApplicationsByJob(
        jobId: String,
        callback: LoadAllApplicationsCallback
    ): LiveData<ApiResponse<List<ApplicationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplication = MutableLiveData<ApiResponse<List<ApplicationResponseEntity>>>()
        applicationHelper.getApplicationsByJob(jobId, object :
            LoadAllApplicationsCallback {
            override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                resultApplication.value =
                    ApiResponse.success(callback.onAllApplicationsReceived(applicationsResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicationsResponse
            }
        })
        return resultApplication
    }

    fun updateApplicationMark(
        applicationId: String,
        isMarked: Boolean,
        callback: UpdateApplicationMarkCallback
    ) {
        EspressoIdlingResource.increment()
        applicationHelper.updateApplicationMark(
            applicationId,
            isMarked,
            object : UpdateApplicationMarkCallback {
                override fun onSuccessUpdateMark() {
                    callback.onSuccessUpdateMark()
                    EspressoIdlingResource.decrement()
                }

                override fun onFailureUpdateMark(messageId: Int) {
                    callback.onFailureUpdateMark(messageId)
                    EspressoIdlingResource.decrement()
                }
            })
    }

    fun updateApplicationStatus(
        applicationId: String,
        status: String,
        callback: UpdateApplicationStatusCallback
    ) {
        EspressoIdlingResource.increment()
        applicationHelper.updateApplicationStatus(
            applicationId,
            status,
            object : UpdateApplicationStatusCallback {
                override fun onSuccessUpdateStatus() {
                    callback.onSuccessUpdateStatus()
                    EspressoIdlingResource.decrement()
                }

                override fun onFailureUpdateStatus(messageId: Int) {
                    callback.onFailureUpdateStatus(messageId)
                    EspressoIdlingResource.decrement()
                }
            })
    }

    fun deleteApplicationsByJob(
        jobId: String,
        callback: DeleteApplicationByJobCallback
    ) {
        EspressoIdlingResource.increment()
        applicationHelper.deleteApplicationsByJob(
            jobId,
            object : DeleteApplicationByJobCallback {
                override fun onSuccessDeleteApplications() {
                    callback.onSuccessDeleteApplications()
                    EspressoIdlingResource.decrement()
                }

                override fun onFailureDeleteApplications(messageId: Int) {
                    callback.onFailureDeleteApplications(messageId)
                    EspressoIdlingResource.decrement()
                }
            })
    }
}