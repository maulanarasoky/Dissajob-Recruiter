package org.d3ifcool.dissajobrecruiter.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.JobResponseEntity
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.JobHelper

class RemoteDataSource private constructor(private val jobHelper: JobHelper) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(jobHelper: JobHelper): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(jobHelper)
            }
    }

    fun getJobs(callback: LoadJobsCallback): LiveData<ApiResponse<List<JobResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultJob = MutableLiveData<ApiResponse<List<JobResponseEntity>>>()
        jobHelper.getJobs(object : LoadJobsCallback {
            override fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity> {
                resultJob.value = ApiResponse.success(callback.onAllJobsReceived(jobResponse))
                EspressoIdlingResource.decrement()
                return jobResponse
            }
        })
        return resultJob
    }

    interface LoadJobsCallback {
        fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity>
    }
}