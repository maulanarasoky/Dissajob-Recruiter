package org.d3ifcool.dissajobrecruiter.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.dissajobrecruiter.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.education.EducationRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.experience.ExperienceRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.interview.InterviewRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.media.MediaRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.notification.NotificationRepository
import org.d3ifcool.dissajobrecruiter.data.source.repository.recruiter.RecruiterRepository
import org.d3ifcool.dissajobrecruiter.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobrecruiter.ui.applicant.education.EducationViewModel
import org.d3ifcool.dissajobrecruiter.ui.applicant.experience.ExperienceViewModel
import org.d3ifcool.dissajobrecruiter.ui.applicant.media.MediaViewModel
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobrecruiter.ui.di.Injection
import org.d3ifcool.dissajobrecruiter.ui.job.JobViewModel
import org.d3ifcool.dissajobrecruiter.ui.notification.NotificationViewModel
import org.d3ifcool.dissajobrecruiter.ui.profile.RecruiterViewModel
import org.d3ifcool.dissajobrecruiter.ui.question.InterviewViewModel
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInViewModel
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpViewModel

class ViewModelFactory private constructor(
    private val jobRepository: JobRepository,
    private val applicationRepository: ApplicationRepository,
    private val interviewRepository: InterviewRepository,
    private val applicantRepository: ApplicantRepository,
    private val mediaRepository: MediaRepository,
    private val experienceRepository: ExperienceRepository,
    private val educationRepository: EducationRepository,
    private val recruiterRepository: RecruiterRepository,
    private val notificationRepository: NotificationRepository
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
                Injection.provideMediaRepository(context),
                Injection.provideExperienceRepository(context),
                Injection.provideEducationRepository(context),
                Injection.provideRecruiterRepository(context),
                Injection.provideNotificationRepository(context)
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
            modelClass.isAssignableFrom(MediaViewModel::class.java) -> {
                MediaViewModel(mediaRepository) as T
            }
            modelClass.isAssignableFrom(ExperienceViewModel::class.java) -> {
                ExperienceViewModel(experienceRepository) as T
            }
            modelClass.isAssignableFrom(EducationViewModel::class.java) -> {
                EducationViewModel(educationRepository) as T
            }
            modelClass.isAssignableFrom(RecruiterViewModel::class.java) -> {
                RecruiterViewModel(recruiterRepository) as T
            }
            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> {
                NotificationViewModel(notificationRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}