package org.d3ifcool.dissajobrecruiter.data.source.local.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.room.JobDao

class LocalJobSource private constructor(
    private val mJobDao: JobDao
) {

    companion object {
        private var INSTANCE: LocalJobSource? = null

        fun getInstance(jobDao: JobDao): LocalJobSource =
            INSTANCE ?: LocalJobSource(jobDao)
    }

    fun getJobs(recruiterId: String): DataSource.Factory<Int, JobEntity> =
        mJobDao.getJobs(recruiterId)

    fun getJobDetails(jobId: String): LiveData<JobDetailsEntity> = mJobDao.getJobDetails(jobId)

    fun insertJob(jobs: List<JobEntity>) = mJobDao.insertJobs(jobs)

    fun insertJobDetails(jobDetails: JobDetailsEntity) = mJobDao.insertJobDetails(jobDetails)

    fun deleteJobItem(jobId: String) = mJobDao.deleteJobItem(jobId)

    fun deleteJobDetails(jobId: String) = mJobDao.deleteJobDetails(jobId)

    fun deleteAllJobs(recruiterId: String) = mJobDao.deleteAllJobs(recruiterId)
}