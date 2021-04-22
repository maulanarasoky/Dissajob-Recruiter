package org.d3ifcool.dissajobrecruiter.ui.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.d3ifcool.dissajobrecruiter.data.source.local.room.DissajobRecruiterDatabase
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalApplicantSource
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalApplicationSource
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalJobSource
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalRecruiterSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteApplicantSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteApplicationSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteRecruiterSource
import org.d3ifcool.dissajobrecruiter.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.recruiter.RecruiterRepository
import org.d3ifcool.dissajobrecruiter.utils.*

object Injection {

    fun provideJobRepository(context: Context): JobRepository {
        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteJobSource.getInstance(JobHelper)
        val localDataSource = LocalJobSource.getInstance(database.jobDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return JobRepository.getInstance(remoteDataSource, localDataSource, appExecutors, callback)
    }

    fun provideApplicationRepository(context: Context): ApplicationRepository {

        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteApplicationSource.getInstance(ApplicationHelper)
        val localDataSource = LocalApplicationSource.getInstance(database.applicationDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return ApplicationRepository.getInstance(
            remoteDataSource,
            localDataSource,
            appExecutors,
            callback
        )
    }

    fun provideApplicantRepository(context: Context): ApplicantRepository {

        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteApplicantSource.getInstance(ApplicantHelper)
        val localDataSource = LocalApplicantSource.getInstance(database.applicantDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return ApplicantRepository.getInstance(
            remoteDataSource,
            localDataSource,
            appExecutors,
            callback
        )
    }

    fun provideUserRepository(context: Context): RecruiterRepository {

        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteRecruiterSource.getInstance(RecruiterHelper)
        val localDataSource = LocalRecruiterSource.getInstance(database.recruiterDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return RecruiterRepository.getInstance(
            remoteDataSource,
            localDataSource,
            appExecutors,
            callback
        )
    }
}