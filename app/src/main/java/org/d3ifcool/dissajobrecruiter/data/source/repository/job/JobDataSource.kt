package org.d3ifcool.dissajobrecruiter.data.source.repository.job

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.callback.CreateJobCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.DeleteJobCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.UpdateJobCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface JobDataSource {
    fun getJobs(): LiveData<Resource<PagedList<JobEntity>>>
    fun getJobDetails(jobId: String): LiveData<Resource<JobDetailsEntity>>
    fun createJob(jobResponse: JobDetailsResponseEntity, callback: CreateJobCallback)
    fun updateJob(jobResponse: JobDetailsResponseEntity, callback: UpdateJobCallback)
    fun deleteJob(jobId: String, callback: DeleteJobCallback)
}