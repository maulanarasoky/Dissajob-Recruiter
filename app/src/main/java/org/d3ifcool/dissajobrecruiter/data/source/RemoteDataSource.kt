package org.d3ifcool.dissajobrecruiter.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.entity.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.entity.JobEntity
import org.d3ifcool.dissajobrecruiter.data.entity.UserEntity
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.JobHelper
import org.d3ifcool.dissajobrecruiter.utils.UserHelper

class RemoteDataSource private constructor(
    private val jobHelper: JobHelper,
    private val userHelper: UserHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(jobHelper: JobHelper, userHelper: UserHelper): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(jobHelper, userHelper)
            }
    }

    fun createUser(
        email: String,
        password: String,
        user: UserEntity,
        callback: SignUpCallback
    ) {
        EspressoIdlingResource.increment()
        userHelper.signUp(email, password, user, object : SignUpCallback {
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

    fun signIn(email: String, password: String, callback: SignInCallback) {
        EspressoIdlingResource.increment()
        userHelper.signIn(email, password, object : SignInCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onNotVerified() {
                callback.onNotVerified()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure() {
                callback.onFailure()
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun createJob(job: JobDetailsEntity, callback: JobPostCallback) {
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

    fun getJobs(callback: LoadJobsCallback): LiveData<List<JobEntity>> {
        EspressoIdlingResource.increment()
        val resultJob = MutableLiveData<List<JobEntity>>()
        jobHelper.getJobs(object : LoadJobsCallback {
            override fun onAllJobsReceived(jobResponse: List<JobEntity>): List<JobEntity> {
                resultJob.value = callback.onAllJobsReceived(jobResponse)
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return jobResponse
            }
        })
        return resultJob
    }

    interface LoadJobsCallback {
        fun onAllJobsReceived(jobResponse: List<JobEntity>): List<JobEntity>
    }
}