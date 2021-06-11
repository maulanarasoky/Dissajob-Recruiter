package org.d3ifcool.dissajobrecruiter.data.source.repository.experience

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalExperienceSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.experience.ExperienceResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteExperienceSource
import org.d3ifcool.dissajobrecruiter.ui.applicant.experience.LoadExperienceCallback
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.NetworkStateCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class ExperienceRepository private constructor(
    private val remoteExperienceSource: RemoteExperienceSource,
    private val localExperienceSource: LocalExperienceSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    ExperienceDataSource {

    companion object {
        @Volatile
        private var instance: ExperienceRepository? = null

        fun getInstance(
            remoteExperience: RemoteExperienceSource,
            localExperience: LocalExperienceSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): ExperienceRepository =
            instance ?: synchronized(this) {
                instance ?: ExperienceRepository(
                    remoteExperience,
                    localExperience,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun getApplicantExperiences(applicantId: String): LiveData<Resource<PagedList<ExperienceEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ExperienceEntity>, List<ExperienceResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ExperienceEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localExperienceSource.getApplicantExperiences(
                        applicantId
                    ), config
                ).build()
            }

            override fun shouldFetch(data: PagedList<ExperienceEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()
//                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<ExperienceResponseEntity>>> =
                remoteExperienceSource.getApplicantExperiences(
                    applicantId,
                    object : LoadExperienceCallback {
                        override fun onAllExperiencesReceived(experienceResponse: List<ExperienceResponseEntity>): List<ExperienceResponseEntity> {
                            return experienceResponse
                        }
                    })

            public override fun saveCallResult(data: List<ExperienceResponseEntity>) {
                val experienceList = ArrayList<ExperienceEntity>()
                for (response in data) {
                    val experience = ExperienceEntity(
                        response.id,
                        response.title,
                        response.employmentType,
                        response.companyName,
                        response.location,
                        response.startMonth,
                        response.startYear,
                        response.endMonth,
                        response.endYear,
                        response.description,
                        response.isCurrentlyWorking,
                        response.applicantId
                    )
                    experienceList.add(experience)
                }

                localExperienceSource.insertApplicantExperiences(experienceList)
            }
        }.asLiveData()
    }
}