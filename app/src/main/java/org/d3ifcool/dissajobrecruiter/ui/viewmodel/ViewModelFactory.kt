package org.d3ifcool.dissajobrecruiter.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.dissajobrecruiter.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.interview.InterviewRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.recruiter.RecruiterRepository
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobrecruiter.ui.di.Injection
import org.d3ifcool.dissajobrecruiter.ui.job.JobViewModel
import org.d3ifcool.dissajobrecruiter.ui.profile.RecruiterViewModel
import org.d3ifcool.dissajobrecruiter.ui.question.InterviewViewModel
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInViewModel
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpViewModel

class ViewModelFactory private constructor(
    private val jobRepository: JobRepository,
    private val applicationRepository: ApplicationRepository,
    private val interviewRepository: InterviewRepository,
    private val applicantRepository: ApplicantRepository,
    private val recruiterRepository: RecruiterRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideJobRepository(context),
                Injection.provideApplicationRepository(context),
                Injection.provideInterviewRepository(context),
                Injection.provideApplicantRepository(context),
                Injection.provideUserRepository(context)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(JobViewModel::class.java) -> {
                JobViewModel(jobRepository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(recruiterRepository) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(recruiterRepository) as T
            }
            modelClass.isAssignableFrom(ApplicationViewModel::class.java) -> {
                ApplicationViewModel(applicationRepository) as T
            }
            modelClass.isAssignableFrom(InterviewViewModel::class.java) -> {
                InterviewViewModel(interviewRepository) as T
            }
            modelClass.isAssignableFrom(ApplicantViewModel::class.java) -> {
                ApplicantViewModel(applicantRepository) as T
            }
            modelClass.isAssignableFrom(RecruiterViewModel::class.java) -> {
                RecruiterViewModel(recruiterRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}