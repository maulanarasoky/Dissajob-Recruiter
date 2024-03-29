package org.d3ifcool.dissajobrecruiter.data.source.repository.education

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalEducationSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteEducationSource
import org.d3ifcool.dissajobrecruiter.ui.applicant.education.LoadEducationsCallback
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.vo.Resource

class FakeEducationRepository(
    private val remoteEducationSource: RemoteEducationSource,
    private val localEducationSource: LocalEducationSource,
    private val appExecutors: AppExecutors
) :
    EducationDataSource {

    override fun getApplicantEducations(applicantId: String): LiveData<Resource<PagedList<EducationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<EducationEntity>, List<EducationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<EducationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localEducationSource.getApplicantEducations(applicantId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<EducationEntity>?): Boolean =
                data == null

            public override fun createCall(): LiveData<ApiResponse<List<EducationResponseEntity>>> =
                remoteEducationSource.getApplicantEducations(
                    applicantId,
                    object : LoadEducationsCallback {
                        override fun onAllEducationsReceived(educationResponse: List<EducationResponseEntity>): List<EducationResponseEntity> {
                            return educationResponse
                        }
                    })

            public override fun saveCallResult(data: List<EducationResponseEntity>) {
                localEducationSource.deleteAllApplicantEducations(applicantId)
                val educationList = ArrayList<EducationEntity>()
                for (response in data) {
                    val education = EducationEntity(
                        response.id,
                        response.schoolName,
                        response.educationLevel,
                        response.fieldOfStudy,
                        response.startMonth,
                        response.startYear,
                        response.endMonth,
                        response.endYear,
                        response.description,
                        response.applicantId
                    )
                    educationList.add(education)
                }
                localEducationSource.insertApplicantEducations(educationList)
            }
        }.asLiveData()
    }
}