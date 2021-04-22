package org.d3ifcool.dissajobrecruiter.data.source.repository.applicant

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface ApplicantDataSource {
    fun getApplicantDetails(applicantId: String): LiveData<Resource<ApplicantEntity>>
}