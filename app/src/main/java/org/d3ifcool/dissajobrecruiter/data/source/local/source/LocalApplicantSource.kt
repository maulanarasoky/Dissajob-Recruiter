package org.d3ifcool.dissajobrecruiter.data.source.local.source

import androidx.lifecycle.LiveData
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

    fun getApplicantDetails(applicantId: String): LiveData<ApplicantEntity> =
        mApplicantDao.getApplicantDetails(applicantId)

    fun insertApplicant(applicantDetails: ApplicantEntity) =
        mApplicantDao.insertApplicant(applicantDetails)
}