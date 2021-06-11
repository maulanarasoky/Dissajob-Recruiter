package org.d3ifcool.dissajobrecruiter.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs")
    fun getJobs(): DataSource.Factory<Int, JobEntity>

    @Query("SELECT * FROM job_details WHERE id = :jobId")
    fun getJobDetails(jobId: String): LiveData<JobDetailsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJobs(jobs: List<JobEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJobDetails(jobDetails: JobDetailsEntity)

    @Query("DELETE FROM jobs WHERE id = :jobId")
    fun deleteJobItem(jobId: String)

    @Query("DELETE FROM job_details WHERE id = :jobId")
    fun deleteJobDetails(jobId: String)

    @Query("DELETE FROM jobs")
    fun deleteAllJobs()
}