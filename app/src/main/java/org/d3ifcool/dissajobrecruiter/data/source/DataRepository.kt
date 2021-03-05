package org.d3ifcool.dissajobrecruiter.data.source

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.LocalDataSource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.RemoteDataSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.JobResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.NetworkStateCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class DataRepository private constructor(
    private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors, private val networkCallback: NetworkStateCallback
) :
    JobDataSource, UserDataSource {

    companion object {
        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(remoteData, localData, appExecutors, networkCallback)
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
                return LivePagedListBuilder(localDataSource.getJobs(), config).build()
            }

            override fun shouldFetch(data: PagedList<JobEntity>?): Boolean =
                networkCallback.isConnected()
//                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<JobResponseEntity>>> =
                remoteDataSource.getJobs(object : RemoteDataSource.LoadJobsCallback {
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

                localDataSource.insertJob(jobList)
            }
        }.asLiveData()
    }

    override fun createJob(jobResponse: JobDetailsResponseEntity, callback: JobPostCallback) =
        appExecutors.diskIO().execute { remoteDataSource.createJob(jobResponse, callback) }

    override fun createUser(
        email: String,
        password: String,
        user: UserResponseEntity,
        callback: SignUpCallback
    ) = appExecutors.diskIO()
        .execute { remoteDataSource.createUser(email, password, user, callback) }

    override fun signIn(email: String, password: String, callback: SignInCallback) =
        appExecutors.diskIO()
            .execute { remoteDataSource.signIn(email, password, callback) }
}