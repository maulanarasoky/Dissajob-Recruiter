package org.d3ifcool.dissajobrecruiter.data.source.repository.experience

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface ExperienceDataSource {
    fun getApplicantExperiences(applicantId: String): LiveData<Resource<PagedList<ExperienceEntity>>>
}