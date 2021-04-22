package org.d3ifcool.dissajobrecruiter.data.source.local.source

import androidx.paging.DataSource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.room.ApplicantDao
import org.d3ifcool.dissajobrecruiter.data.source.local.room.ApplicationDao

class LocalApplicationSource private constructor(
    private val mApplicationDao: ApplicationDao
) {

    companion object {
        private var INSTANCE: LocalApplicationSource? = null

        fun getInstance(applicationDao: ApplicationDao): LocalApplicationSource =
            INSTANCE ?: LocalApplicationSource(applicationDao)
    }

    fun getApplications(): DataSource.Factory<Int, ApplicationEntity> = mApplicationDao.getApplications()

    fun getAcceptedApplications(): DataSource.Factory<Int, ApplicationEntity> = mApplicationDao.getApplicationsByStatus("accepted")

    fun getRejectedApplications(): DataSource.Factory<Int, ApplicationEntity> = mApplicationDao.getApplicationsByStatus("rejected")

    fun getMarkedApplications(): DataSource.Factory<Int, ApplicationEntity> = mApplicationDao.getMarkedApplications()

    fun getApplicationsByJob(jobId: String): DataSource.Factory<Int, ApplicationEntity> = mApplicationDao.getApplicationsByJob(jobId)

    fun insertApplication(applications: List<ApplicationEntity>) =
        mApplicationDao.insertApplication(applications)
}