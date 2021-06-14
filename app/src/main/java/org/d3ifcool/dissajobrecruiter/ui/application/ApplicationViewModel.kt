package org.d3ifcool.dissajobrecruiter.ui.application

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobrecruiter.ui.application.callback.DeleteApplicationByJobCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.UpdateApplicationMarkCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.UpdateApplicationStatusCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class ApplicationViewModel(private val applicationRepository: ApplicationRepository) : ViewModel() {
    fun getApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getApplications(recruiterId)

    fun getApplicationById(applicationId: String): LiveData<Resource<ApplicationEntity>> =
        applicationRepository.getApplicationById(applicationId)

    fun getAcceptedApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getAcceptedApplications(recruiterId)

    fun getRejectedApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getRejectedApplications(recruiterId)

    fun getMarkedApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getMarkedApplications(recruiterId)

    fun getApplicationsByJob(jobId: String): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getApplicationsByJob(jobId)

    fun updateApplicationMark(
        applicationId: String,
        isMarked: Boolean,
        callback: UpdateApplicationMarkCallback
    ) = applicationRepository.updateApplicationMark(applicationId, isMarked, callback)

    fun updateApplicationStatus(
        applicationId: String,
        status: String,
        callback: UpdateApplicationStatusCallback
    ) = applicationRepository.updateApplicationStatus(applicationId, status, callback)

    fun deleteApplicationsByJob(jobId: String, callback: DeleteApplicationByJobCallback) =
        applicationRepository.deleteApplicationsByJob(jobId, callback)
}