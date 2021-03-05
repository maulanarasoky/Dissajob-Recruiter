package org.d3ifcool.dissajobrecruiter.data.source.local

import androidx.paging.DataSource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.room.JobDao
import org.d3ifcool.dissajobrecruiter.data.source.local.room.UserDao

class LocalDataSource private constructor(
    private val mJobDao: JobDao,
    private val mUserDao: UserDao
) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(jobDao: JobDao, userDao: UserDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(jobDao, userDao)
    }

    fun getJobs(): DataSource.Factory<Int, JobEntity> = mJobDao.getJobs()

    fun insertJob(jobs: List<JobEntity>) = mJobDao.insertJobs(jobs)
}