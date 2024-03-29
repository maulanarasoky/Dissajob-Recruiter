package org.d3ifcool.dissajobrecruiter.data.source.repository.job

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalJobSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobrecruiter.ui.job.callback.*
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.NetworkStateCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class JobRepository private constructor(
    private val remoteJobSource: RemoteJobSource, private val localJobSource: LocalJobSource,
    private val appExecutors: AppExecutors, private val networkCallback: NetworkStateCallback
) :
    JobDataSource {

    companion object {
        @Volatile
        private var instance: JobRepository? = null

        fun getInstance(
            remoteJob: RemoteJobSource,
            localJob: LocalJobSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): JobRepository =
            instance ?: synchronized(this) {
                instance ?: JobRepository(remoteJob, localJob, appExecutors, networkCallback)
            }
    }

    override fun getJobs(recruiterId: String): LiveData<Resource<PagedList<JobEntity>>> {
        return object :
            NetworkBoundResource<PagedList<JobEntity>, List<JobResponseEntity>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<JobEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localJobSource.getJobs(recruiterId), config).build()
            }

            override fun shouldFetch(data: PagedList<JobEntity>?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<List<JobResponseEntity>>> =
                remoteJobSource.getJobs(recruiterId, object : LoadJobsCallback {
                    override fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity> {
                        return jobResponse
                    }
                })

            public override fun saveCallResult(data: List<JobResponseEntity>) {
                localJobSource.deleteAllJobs(recruiterId)
                val jobList = ArrayList<JobEntity>()
                for (response in data) {
                    val job = JobEntity(
                        response.id,
                        response.title,
                        response.description,
                        response.postedBy,
                        response.postedDate,
                        response.isOpen,
                        response.isOpenForDisability
                    )
                    jobList.add(job)
                }
                localJobSource.insertJob(jobList)
            }
        }.asLiveData()
    }

    override fun getJobDetails(jobId: String): LiveData<Resource<JobDetailsEntity>> {
        return object :
            NetworkBoundResource<JobDetailsEntity, JobDetailsResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<JobDetailsEntity> =
                localJobSource.getJobDetails(jobId)

            override fun shouldFetch(data: JobDetailsEntity?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<JobDetailsResponseEntity>> =
                remoteJobSource.getJobDetails(
                    jobId,
                    object : LoadJobDetailsCallback {
                        override fun onJobDetailsReceived(jobResponse: JobDetailsResponseEntity): JobDetailsResponseEntity {
                            return jobResponse
                        }
                    })

            public override fun saveCallResult(data: JobDetailsResponseEntity) {
                val jobDetails = JobDetailsEntity(
                    data.id,
                    data.title,
                    data.description,
                    data.address,
                    data.qualification,
                    data.employment,
                    data.type,
                    data.industry,
                    data.salary,
                    data.postedBy,
                    data.postedDate,
                    data.updatedDate,
                    data.closedDate,
                    data.isOpen,
                    data.isOpenForDisability,
                    data.additionalInformation
                )
                localJobSource.insertJobDetails(jobDetails)
            }
        }.asLiveData()
    }

    override fun createJob(jobResponse: JobDetailsResponseEntity, callback: CreateJobCallback) =
        appExecutors.diskIO().execute { remoteJobSource.createJob(jobResponse, callback) }

    override fun updateJob(jobResponse: JobDetailsResponseEntity, callback: UpdateJobCallback) =
        appExecutors.diskIO().execute { remoteJobSource.updateJob(jobResponse, callback) }

    override fun deleteJob(jobId: String, callback: DeleteJobCallback) =
        appExecutors.diskIO().execute {
            remoteJobSource.deleteJob(jobId, callback)
            localJobSource.deleteJobItem(jobId)
            localJobSource.deleteJobDetails(jobId)
        }

    override fun deleteSavedJobByJob(jobId: String, callback: DeleteSavedJobCallback) =
        appExecutors.diskIO().execute {
            remoteJobSource.deleteSavedJobByJob(jobId, callback)
        }
}