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

    fun getJobs(): DataSource.Factory<Int, JobEntity> = mJobDao.getJobs()

    fun getJobDetails(jobId: String): LiveData<JobDetailsEntity> = mJobDao.getJobDetails(jobId)

    fun insertJob(jobs: List<JobEntity>) = mJobDao.insertJobs(jobs)

    fun insertJobDetails(jobDetails: JobDetailsEntity) = mJobDao.insertJobDetails(jobDetails)

    fun updateJobDetails(jobDetails: JobDetailsEntity) = mJobDao.updateJobDetails(jobDetails)
}