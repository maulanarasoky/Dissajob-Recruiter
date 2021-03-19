package org.d3ifcool.dissajobrecruiter.data.source.repository.applicant

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalApplicantSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant.ApplicantDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteApplicantSource
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.NetworkStateCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class ApplicantRepository private constructor(
    private val remoteApplicantSource: RemoteApplicantSource,
    private val localApplicantSource: LocalApplicantSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    ApplicantDataSource {

    companion object {
        @Volatile
        private var instance: ApplicantRepository? = null

        fun getInstance(
            remoteApplicant: RemoteApplicantSource,
            localApplicant: LocalApplicantSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): ApplicantRepository =
            instance ?: synchronized(this) {
                instance ?: ApplicantRepository(
                    remoteApplicant,
                    localApplicant,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun getApplicants(): LiveData<Resource<PagedList<ApplicantEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicantEntity>, List<ApplicantResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicantEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localApplicantSource.getApplicants(), config).build()
            }

            override fun shouldFetch(data: PagedList<ApplicantEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<List<ApplicantResponseEntity>>> =
                remoteApplicantSource.getAllApplicants(object :
                    RemoteApplicantSource.LoadAllApplicantsCallback {
                    override fun onAllApplicantsReceived(applicantsResponse: List<ApplicantResponseEntity>): List<ApplicantResponseEntity> {
                        return applicantsResponse
                    }
                })

            public override fun saveCallResult(data: List<ApplicantResponseEntity>) {
                val applicantList = ArrayList<ApplicantEntity>()
                for (response in data) {
                    val applicant = ApplicantEntity(
                        response.id.toString(),
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.status,
                        response.isMarked
                    )
                    applicantList.add(applicant)
                }

                localApplicantSource.insertApplicant(applicantList)
            }
        }.asLiveData()
    }

    override fun getAcceptedApplicants(): LiveData<Resource<PagedList<ApplicantEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicantEntity>, List<ApplicantResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicantEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localApplicantSource.getAcceptedApplicants(), config).build()
            }

            override fun shouldFetch(data: PagedList<ApplicantEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<List<ApplicantResponseEntity>>> =
                remoteApplicantSource.getAcceptedApplicants(object :
                    RemoteApplicantSource.LoadAllApplicantsCallback {
                    override fun onAllApplicantsReceived(applicantsResponse: List<ApplicantResponseEntity>): List<ApplicantResponseEntity> {
                        return applicantsResponse
                    }
                })

            public override fun saveCallResult(data: List<ApplicantResponseEntity>) {
                val applicantList = ArrayList<ApplicantEntity>()
                for (response in data) {
                    val applicant = ApplicantEntity(
                        response.id.toString(),
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.status,
                        response.isMarked
                    )
                    applicantList.add(applicant)
                }

                localApplicantSource.insertApplicant(applicantList)
            }
        }.asLiveData()
    }

    override fun getApplicantDetails(applicantId: String): LiveData<Resource<ApplicantDetailsEntity>> {
        return object :
            NetworkBoundResource<ApplicantDetailsEntity, ApplicantDetailsResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<ApplicantDetailsEntity> =
                localApplicantSource.getApplicantDetails(applicantId)

            override fun shouldFetch(data: ApplicantDetailsEntity?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<ApplicantDetailsResponseEntity>> =
                remoteApplicantSource.getApplicantDetails(
                    applicantId,
                    object : RemoteApplicantSource.LoadApplicantDetailsCallback {
                        override fun onApplicantDetailsReceived(applicantDetailsResponse: ApplicantDetailsResponseEntity): ApplicantDetailsResponseEntity {
                            return applicantDetailsResponse
                        }
                    })

            public override fun saveCallResult(data: ApplicantDetailsResponseEntity) {
                val applicantDetailsEntity = ApplicantDetailsEntity(
                    data.id.toString(),
                    data.firstName,
                    data.lastName,
                    data.aboutMe,
                    data.phoneNumber,
                    data.imagePath
                )
                localApplicantSource.insertApplicantDetails(applicantDetailsEntity)
            }
        }.asLiveData()
    }
}