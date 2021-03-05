package org.d3ifcool.dissajobrecruiter.ui.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.DataRepository
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.vo.Resource

class JobViewModel(private val dataRepository: DataRepository) : ViewModel() {
    fun getJobs(): LiveData<Resource<PagedList<JobEntity>>> = dataRepository.getJobs()
    fun createJob(
        jobResponse: JobDetailsResponseEntity,
        callback: JobPostCallback
    ) = dataRepository.createJob(jobResponse, callback)
}