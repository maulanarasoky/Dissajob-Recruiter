package org.d3ifcool.dissajobrecruiter.ui.applicant.experience

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.experience.ExperienceRepository
import org.d3ifcool.dissajobrecruiter.vo.Resource

class ExperienceViewModel(private val experienceRepository: ExperienceRepository) : ViewModel() {
    fun getApplicantExperiences(applicantId: String): LiveData<Resource<PagedList<ExperienceEntity>>> =
        experienceRepository.getApplicantExperiences(applicantId)
}