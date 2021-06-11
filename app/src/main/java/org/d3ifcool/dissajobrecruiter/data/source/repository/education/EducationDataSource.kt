package org.d3ifcool.dissajobrecruiter.data.source.repository.education

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface EducationDataSource {
    fun getApplicantEducations(applicantId: String): LiveData<Resource<PagedList<EducationEntity>>>
}