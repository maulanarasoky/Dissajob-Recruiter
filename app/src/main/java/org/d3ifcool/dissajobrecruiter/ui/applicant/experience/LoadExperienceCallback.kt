package org.d3ifcool.dissajobrecruiter.ui.applicant.experience

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.experience.ExperienceResponseEntity

interface LoadExperienceCallback {
    fun onAllExperiencesReceived(experienceResponse: List<ExperienceResponseEntity>): List<ExperienceResponseEntity>
}