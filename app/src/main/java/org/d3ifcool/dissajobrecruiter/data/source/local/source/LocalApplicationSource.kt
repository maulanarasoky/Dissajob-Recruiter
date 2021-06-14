package org.d3ifcool.dissajobrecruiter.data.source.local.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.room.ApplicationDao

class LocalApplicationSource private constructor(
    private val mApplicationDao: ApplicationDao
) {

    companion object {
        private var INSTANCE: LocalApplicationSource? = null

        fun getInstance(applicationDao: ApplicationDao): LocalApplicationSource =
            INSTANCE ?: LocalApplicationSource(applicationDao)
    }

    fun getApplications(recruiterId: String): DataSource.Factory<Int, ApplicationEntity> =
        mApplicationDao.getApplications(recruiterId)

    fun getApplicationById(applicationId: String): LiveData<ApplicationEntity> =
        mApplicationDao.getApplicationById(applicationId)

    fun getAcceptedApplications(recruiterId: String): DataSource.Factory<Int, ApplicationEntity> =
        mApplicationDao.getApplicationsByStatus(recruiterId, "Accepted")

    fun getRejectedApplications(recruiterId: String): DataSource.Factory<Int, ApplicationEntity> =
        mApplicationDao.getApplicationsByStatus(recruiterId, "Rejected")

    fun getMarkedApplications(recruiterId: String): DataSource.Factory<Int, ApplicationEntity> =
        mApplicationDao.getMarkedApplications(recruiterId)

    fun getApplicationsByJob(jobId: String): DataSource.Factory<Int, ApplicationEntity> =
        mApplicationDao.getApplicationsByJob(jobId)

    fun updateApplicationMark(applicationId: String, isMarked: Boolean) =
        mApplicationDao.updateApplicationMark(applicationId, isMarked)

    fun updateApplicationStatus(applicationId: String, status: String) =
        mApplicationDao.updateApplicationStatus(applicationId, status)

    fun deleteApplicationsByJob(jobId: String) =
        mApplicationDao.deleteApplicationsByJob(jobId)

    fun deleteAllApplications() = mApplicationDao.deleteAllApplications()

    fun insertApplication(applications: List<ApplicationEntity>) =
        mApplicationDao.insertApplication(applications)
}