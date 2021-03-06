package org.d3ifcool.dissajobrecruiter.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.dissajobrecruiter.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.user.UserRepository
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.di.Injection
import org.d3ifcool.dissajobrecruiter.ui.job.JobViewModel
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInViewModel
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpViewModel

class ViewModelFactory private constructor(
    private val jobRepository: JobRepository,
    private val applicantRepository: ApplicantRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideJobRepository(context),
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
                SignUpViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ApplicantViewModel::class.java) -> {
                ApplicantViewModel(applicantRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}