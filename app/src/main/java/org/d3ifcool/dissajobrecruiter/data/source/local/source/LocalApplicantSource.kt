package org.d3ifcool.dissajobrecruiter.data.source.local.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.room.ApplicantDao

class LocalApplicantSource private constructor(
    private val mApplicantDao: ApplicantDao
) {

    companion object {
        private var INSTANCE: LocalApplicantSource? = null

        fun getInstance(applicantDao: ApplicantDao): LocalApplicantSource =
            INSTANCE ?: LocalApplicantSource(applicantDao)
    }

    fun getApplicants(): DataSource.Factory<Int, ApplicantEntity> = mApplicantDao.getApplicants()

    fun getAcceptedApplicants(): DataSource.Factory<Int, ApplicantEntity> = mApplicantDao.getApplicantsByStatus("accepted")

    fun getRejectedApplicants(): DataSource.Factory<Int, ApplicantEntity> = mApplicantDao.getApplicantsByStatus("rejected")

    fun getMarkedApplicants(): DataSource.Factory<Int, ApplicantEntity> = mApplicantDao.getMarkedApplicants()

    fun getApplicantsByJob(jobId: String): DataSource.Factory<Int, ApplicantEntity> = mApplicantDao.getApplicantsByJob(jobId)

    fun getApplicantDetails(applicantId: String): LiveData<ApplicantDetailsEntity> =
        mApplicantDao.getApplicantDetails(applicantId)

    fun insertApplicant(applicants: List<ApplicantEntity>) =
        mApplicantDao.insertApplicants(applicants)

    fun insertApplicantDetails(applicantDetails: ApplicantDetailsEntity) =
        mApplicantDao.insertApplicantDetails(applicantDetails)
}