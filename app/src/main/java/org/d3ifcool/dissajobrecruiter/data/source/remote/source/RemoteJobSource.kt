package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.callback.*
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.database.JobHelper

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

    fun createJob(job: JobDetailsResponseEntity, callback: CreateJobCallback) {
        EspressoIdlingResource.increment()
        jobHelper.createJob(job, object : CreateJobCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
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

    fun getJobDetails(
        jobId: String,
        callback: LoadJobDetailsCallback
    ): LiveData<ApiResponse<JobDetailsResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultJob = MutableLiveData<ApiResponse<JobDetailsResponseEntity>>()
        jobHelper.getJobDetails(jobId, object : LoadJobDetailsCallback {
            override fun onJobDetailsReceived(jobResponse: JobDetailsResponseEntity): JobDetailsResponseEntity {
                resultJob.value = ApiResponse.success(callback.onJobDetailsReceived(jobResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return jobResponse
            }
        })
        return resultJob
    }

    fun updateJob(job: JobDetailsResponseEntity, callback: UpdateJobCallback) {
        EspressoIdlingResource.increment()
        jobHelper.updateJob(job, object : UpdateJobCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun deleteJob(jobId: String, callback: DeleteJobCallback) {
        EspressoIdlingResource.increment()
        jobHelper.deleteJob(jobId, object : DeleteJobCallback {
            override fun onSuccessDeleteJob() {
                callback.onSuccessDeleteJob()
                EspressoIdlingResource.decrement()
            }

            override fun onFailureDeleteJob(messageId: Int) {
                callback.onFailureDeleteJob(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun deleteSavedJobByJob(jobId: String, callback: DeleteSavedJobCallback) {
        EspressoIdlingResource.increment()
        jobHelper.deleteSavedJobByJob(jobId, object : DeleteSavedJobCallback {
            override fun onSuccessDeleteSavedJob() {
                callback.onSuccessDeleteSavedJob()
                EspressoIdlingResource.decrement()
            }

            override fun onFailureDeleteSavedJob(messageId: Int) {
                callback.onFailureDeleteSavedJob(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }
}