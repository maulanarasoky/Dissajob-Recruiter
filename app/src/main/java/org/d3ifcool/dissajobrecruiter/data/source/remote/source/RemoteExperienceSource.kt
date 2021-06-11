package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.experience.ExperienceResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.applicant.experience.LoadExperienceCallback
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.database.ExperienceHelper

class RemoteExperienceSource private constructor(
    private val experienceHelper: ExperienceHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteExperienceSource? = null

        fun getInstance(experienceHelper: ExperienceHelper): RemoteExperienceSource =
            instance ?: synchronized(this) {
                instance ?: RemoteExperienceSource(experienceHelper)
            }
    }

    fun getApplicantExperiences(
        applicantId: String,
        callback: LoadExperienceCallback
    ): LiveData<ApiResponse<List<ExperienceResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultExperience = MutableLiveData<ApiResponse<List<ExperienceResponseEntity>>>()
        experienceHelper.getApplicantExperiences(applicantId, object : LoadExperienceCallback {
            override fun onAllExperiencesReceived(experienceResponse: List<ExperienceResponseEntity>): List<ExperienceResponseEntity> {
                resultExperience.value =
                    ApiResponse.success(callback.onAllExperiencesReceived(experienceResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return experienceResponse
            }
        })
        return resultExperience
    }
}