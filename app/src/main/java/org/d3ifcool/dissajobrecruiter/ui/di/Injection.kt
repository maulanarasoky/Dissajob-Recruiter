package org.d3ifcool.dissajobrecruiter.ui.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.d3ifcool.dissajobrecruiter.data.source.local.room.DissajobRecruiterDatabase
import org.d3ifcool.dissajobrecruiter.data.source.local.source.*
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.*
import org.d3ifcool.dissajobrecruiter.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.education.EducationRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.experience.ExperienceRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.interview.InterviewRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.media.MediaRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.notification.NotificationRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.recruiter.RecruiterRepository
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.NetworkStateCallback
import org.d3ifcool.dissajobrecruiter.utils.database.*

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

    fun provideInterviewRepository(context: Context): InterviewRepository {

        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteInterviewSource.getInstance(InterviewHelper)
        val localDataSource = LocalInterviewSource.getInstance(database.interviewDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return InterviewRepository.getInstance(
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

    fun provideMediaRepository(context: Context): MediaRepository {
        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteMediaSource.getInstance(MediaHelper)
        val localDataSource = LocalMediaSource.getInstance(database.mediaDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return MediaRepository.getInstance(
            remoteDataSource,
            localDataSource,
            appExecutors,
            callback
        )
    }

    fun provideExperienceRepository(context: Context): ExperienceRepository {
        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteExperienceSource.getInstance(ExperienceHelper)
        val localDataSource = LocalExperienceSource.getInstance(database.experienceDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return ExperienceRepository.getInstance(
            remoteDataSource,
            localDataSource,
            appExecutors,
            callback
        )
    }

    fun provideEducationRepository(context: Context): EducationRepository {
        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteEducationSource.getInstance(EducationHelper)
        val localDataSource = LocalEducationSource.getInstance(database.educationDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return EducationRepository.getInstance(
            remoteDataSource,
            localDataSource,
            appExecutors,
            callback
        )
    }

    fun provideRecruiterRepository(context: Context): RecruiterRepository {

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

    fun provideNotificationRepository(context: Context): NotificationRepository {

        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteNotificationSource.getInstance(NotificationHelper)
        val localDataSource = LocalNotificationSource.getInstance(database.notificationDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return NotificationRepository.getInstance(
            remoteDataSource,
            localDataSource,
            appExecutors,
            callback
        )
    }
}