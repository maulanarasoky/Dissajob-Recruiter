package org.d3ifcool.dissajobrecruiter.data.source.repository.job

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalJobSource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback
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

    override fun getJobs(): LiveData<Resource<PagedList<JobEntity>>> {
        return object :
            NetworkBoundResource<PagedList<JobEntity>, List<JobResponseEntity>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<JobEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localJobSource.getJobs(), config).build()
            }

            override fun shouldFetch(data: PagedList<JobEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()
//                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<JobResponseEntity>>> =
                remoteJobSource.getJobs(object : RemoteJobSource.LoadJobsCallback {
                    override fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity> {
                        return jobResponse
                    }
                })

            public override fun saveCallResult(data: List<JobResponseEntity>) {
                val jobList = ArrayList<JobEntity>()
                for (response in data) {
                    val job = JobEntity(
                        response.id,
                        response.title,
                        response.description,
                        response.postedBy,
                        response.postedDate,
                        response.isOpen
                    )
                    jobList.add(job)
                }

                localJobSource.insertJob(jobList)
            }
        }.asLiveData()
    }

    override fun createJob(jobResponse: JobDetailsResponseEntity, callback: JobPostCallback) =
        appExecutors.diskIO().execute { remoteJobSource.createJob(jobResponse, callback) }
}