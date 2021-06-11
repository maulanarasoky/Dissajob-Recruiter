package org.d3ifcool.dissajobrecruiter.ui.applicant.education

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.education.EducationRepository
import org.d3ifcool.dissajobrecruiter.vo.Resource

class EducationViewModel(private val educationRepository: EducationRepository) : ViewModel() {
    fun getApplicantEducations(applicantId: String): LiveData<Resource<PagedList<EducationEntity>>> =
        educationRepository.getApplicantEducations(applicantId)
}