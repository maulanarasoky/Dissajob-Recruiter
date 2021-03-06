package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.JobHelper

class RemoteJobSource private constructor(
    private val jobHelper: JobHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteJobSource? = null

        fun getInstance(jobHelper: JobHelper): RemoteJobSource =
            instance ?: synchronized(this) {
                instance ?: RemoteJobSource(jobHelper)
            }
    }

    fun createJob(job: JobDetailsResponseEntity, callback: JobPostCallback) {
        EspressoIdlingResource.increment()
        jobHelper.createJob(job, object : JobPostCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getJobs(callback: LoadJobsCallback): LiveData<ApiResponse<List<JobResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultJob = MutableLiveData<ApiResponse<List<JobResponseEntity>>>()
        jobHelper.getJobs(object : LoadJobsCallback {
            override fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity> {
                resultJob.value = ApiResponse.success(callback.onAllJobsReceived(jobResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return jobResponse
            }
        })
        return resultJob
    }

    interface LoadJobsCallback {
        fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity>
    }
}