package org.d3ifcool.dissajobrecruiter.ui.applicant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobrecruiter.vo.Resource

class ApplicantViewModel(private val applicantRepository: ApplicantRepository) : ViewModel() {
    fun getApplicantDetails(applicantId: String): LiveData<Resource<ApplicantEntity>> =
        applicantRepository.getApplicantDetails(applicantId)
}