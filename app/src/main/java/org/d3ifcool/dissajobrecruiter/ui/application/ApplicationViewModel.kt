package org.d3ifcool.dissajobrecruiter.ui.application

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobrecruiter.vo.Resource

class ApplicationViewModel(private val applicationRepository: ApplicationRepository) : ViewModel() {
    fun getApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getApplications()

    fun getApplicationById(applicationId: String): LiveData<Resource<ApplicationEntity>> =
        applicationRepository.getApplicationById(applicationId)

    fun getAcceptedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getAcceptedApplications()

    fun getRejectedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getRejectedApplications()

    fun getMarkedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getMarkedApplications()

    fun getApplicationsByJob(jobId: String): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getApplicationsByJob(jobId)
}