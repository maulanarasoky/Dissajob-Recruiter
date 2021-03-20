package org.d3ifcool.dissajobrecruiter.data.source.repository.applicant

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface ApplicantDataSource {
    fun getApplicants(): LiveData<Resource<PagedList<ApplicantEntity>>>
    fun getAcceptedApplicants(): LiveData<Resource<PagedList<ApplicantEntity>>>
    fun getRejectedApplicants(): LiveData<Resource<PagedList<ApplicantEntity>>>
    fun getMarkedApplicants(): LiveData<Resource<PagedList<ApplicantEntity>>>
    fun getApplicantDetails(applicantId: String): LiveData<Resource<ApplicantDetailsEntity>>
}