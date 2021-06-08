package org.d3ifcool.dissajobrecruiter.data.source.repository.applicant

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalApplicantSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteApplicantSource
import org.d3ifcool.dissajobrecruiter.ui.applicant.LoadApplicantDetailsCallback
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

    override fun getApplicantDetails(applicantId: String): LiveData<Resource<ApplicantEntity>> {
        return object :
            NetworkBoundResource<ApplicantEntity, ApplicantResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<ApplicantEntity> =
                localApplicantSource.getApplicantDetails(applicantId)

            override fun shouldFetch(data: ApplicantEntity?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<ApplicantResponseEntity>> =
                remoteApplicantSource.getApplicantDetails(applicantId, object : LoadApplicantDetailsCallback {
                    override fun onApplicantDetailsReceived(applicantDetailsResponse: ApplicantResponseEntity): ApplicantResponseEntity {
                        return applicantDetailsResponse
                    }
                })

            public override fun saveCallResult(data: ApplicantResponseEntity) {
                val applicant = ApplicantEntity(
                    data.id,
                    data.firstName,
                    data.lastName,
                    data.fullName,
                    data.email,
                    data.aboutMe,
                    data.phoneNumber,
                    data.imagePath
                )
                localApplicantSource.insertApplicant(applicant)
            }
        }.asLiveData()
    }
}