package org.d3ifcool.dissajobrecruiter.ui.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.entity.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.DataRepository
import org.d3ifcool.dissajobrecruiter.data.entity.JobEntity

class JobViewModel(private val dataRepository: DataRepository) : ViewModel() {
    fun getJobs(): LiveData<List<JobEntity>> = dataRepository.getJobs()
    fun createJob(
        job: JobDetailsEntity,
        callback: JobPostCallback
    ) = dataRepository.createJob(job, callback)
}