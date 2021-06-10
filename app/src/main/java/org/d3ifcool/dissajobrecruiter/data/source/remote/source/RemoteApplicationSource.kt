package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.application.callback.LoadAllApplicationsCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.LoadApplicationDataCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.UpdateApplicationCallback
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

    fun getAllApplications(callback: LoadAllApplicationsCallback): LiveData<ApiResponse<List<ApplicationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplication = MutableLiveData<ApiResponse<List<ApplicationResponseEntity>>>()
        applicationHelper.getAllApplications(object : LoadAllApplicationsCallback {
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

    fun getAcceptedApplications(callback: LoadAllApplicationsCallback): LiveData<ApiResponse<List<ApplicationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplication = MutableLiveData<ApiResponse<List<ApplicationResponseEntity>>>()
        applicationHelper.getAllApplicationsByStatus("accepted", object :
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

    fun getRejectedApplications(callback: LoadAllApplicationsCallback): LiveData<ApiResponse<List<ApplicationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplication = MutableLiveData<ApiResponse<List<ApplicationResponseEntity>>>()
        applicationHelper.getAllApplicationsByStatus("rejected", object :
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

    fun getMarkedApplications(callback: LoadAllApplicationsCallback): LiveData<ApiResponse<List<ApplicationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultApplication = MutableLiveData<ApiResponse<List<ApplicationResponseEntity>>>()
        applicationHelper.getMarkedApplications(object :
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

    fun updateApplicationStatus(
        applicationId: String,
        status: String,
        callback: UpdateApplicationCallback
    ) {
        EspressoIdlingResource.increment()
        applicationHelper.updateApplicationStatus(
            applicationId,
            status,
            object : UpdateApplicationCallback {
                override fun onSuccessUpdate() {
                    callback.onSuccessUpdate()
                    EspressoIdlingResource.decrement()
                }

                override fun onFailureUpdate(messageId: Int) {
                    callback.onFailureUpdate(messageId)
                    EspressoIdlingResource.decrement()
                }
            })
    }
}