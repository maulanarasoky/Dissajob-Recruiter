package org.d3ifcool.dissajobrecruiter.ui.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.d3ifcool.dissajobrecruiter.data.source.local.room.DissajobRecruiterDatabase
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalApplicantSource
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalJobSource
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalUserSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteApplicantSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteUserSource
import org.d3ifcool.dissajobrecruiter.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.user.UserRepository
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

    fun provideUserRepository(context: Context): UserRepository {

        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteUserSource.getInstance(UserHelper)
        val localDataSource = LocalUserSource.getInstance(database.userDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return UserRepository.getInstance(
            remoteDataSource,
            localDataSource,
            appExecutors,
            callback
        )
    }
}