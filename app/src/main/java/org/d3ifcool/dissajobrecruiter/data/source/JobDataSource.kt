package org.d3ifcool.dissajobrecruiter.data.source

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobrecruiter.data.entity.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.entity.JobEntity
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback

interface JobDataSource {
    fun getJobs(): LiveData<List<JobEntity>>
    fun createJob(job: JobDetailsEntity, callback: JobPostCallback)
}