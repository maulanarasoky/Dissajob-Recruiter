package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.application.LoadAllApplicationsCallback
import org.d3ifcool.dissajobrecruiter.utils.ApplicationHelper
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource

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
}