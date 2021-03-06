package org.d3ifcool.dissajobrecruiter.data.source.local.source

import androidx.paging.DataSource
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

    fun insertJob(jobs: List<JobEntity>) = mJobDao.insertJobs(jobs)
}