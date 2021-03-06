package org.d3ifcool.dissajobrecruiter.data.source.repository.job

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface JobDataSource {
    fun getJobs(): LiveData<Resource<PagedList<JobEntity>>>
    fun createJob(jobResponse: JobDetailsResponseEntity, callback: JobPostCallback)
}