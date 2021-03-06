package org.d3ifcool.dissajobrecruiter.ui.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.vo.Resource

class JobViewModel(private val jobRepository: JobRepository) : ViewModel() {
    fun getJobs(): LiveData<Resource<PagedList<JobEntity>>> = jobRepository.getJobs()
    fun createJob(
        jobResponse: JobDetailsResponseEntity,
        callback: JobPostCallback
    ) = jobRepository.createJob(jobResponse, callback)
}