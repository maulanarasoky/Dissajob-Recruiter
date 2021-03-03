package org.d3ifcool.dissajobrecruiter.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.entity.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.entity.JobEntity
import org.d3ifcool.dissajobrecruiter.data.entity.UserEntity
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors

class DataRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val appExecutors: AppExecutors
) :
    JobDataSource, UserDataSource {

    companion object {
        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            appExecutors: AppExecutors
        ): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(remoteData, appExecutors)
            }
    }

    override fun getJobs(): LiveData<List<JobEntity>> {
        val jobLiveData = MutableLiveData<List<JobEntity>>()
        remoteDataSource.getJobs(object : RemoteDataSource.LoadJobsCallback {
            override fun onAllJobsReceived(jobResponse: List<JobEntity>): List<JobEntity> {
                jobLiveData.postValue(jobResponse)
                return jobResponse
            }
        })
        return jobLiveData
    }

    override fun createJob(job: JobDetailsEntity, callback: JobPostCallback) =
        appExecutors.diskIO().execute { remoteDataSource.createJob(job, callback) }

    override fun createUser(
        email: String,
        password: String,
        user: UserEntity,
        callback: SignUpCallback
    ) = appExecutors.diskIO()
        .execute { remoteDataSource.createUser(email, password, user, callback) }

    override fun signIn(email: String, password: String, callback: SignInCallback) =
        appExecutors.diskIO()
            .execute { remoteDataSource.signIn(email, password, callback) }
}