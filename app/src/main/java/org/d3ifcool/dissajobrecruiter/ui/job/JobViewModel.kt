package org.d3ifcool.dissajobrecruiter.ui.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobrecruiter.ui.job.callback.CreateJobCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.DeleteJobCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.DeleteSavedJobCallback
import org.d3ifcool.dissajobrecruiter.ui.job.callback.UpdateJobCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class JobViewModel(private val jobRepository: JobRepository) : ViewModel() {
    fun getJobs(recruiterId: String): LiveData<Resource<PagedList<JobEntity>>> =
        jobRepository.getJobs(recruiterId)

    fun getJobDetails(jobId: String): LiveData<Resource<JobDetailsEntity>> =
        jobRepository.getJobDetails(jobId)

    fun createJob(
        jobResponse: JobDetailsResponseEntity,
        callback: CreateJobCallback
    ) = jobRepository.createJob(jobResponse, callback)

    fun updateJob(
        jobResponse: JobDetailsResponseEntity,
        callback: UpdateJobCallback
    ) = jobRepository.updateJob(jobResponse, callback)

    fun deleteJob(
        jobId: String,
        callback: DeleteJobCallback
    ) = jobRepository.deleteJob(jobId, callback)

    fun deleteSavedJobByJob(
        jobId: String,
        callback: DeleteSavedJobCallback
    ) = jobRepository.deleteSavedJobByJob(jobId, callback)
}