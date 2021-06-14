package org.d3ifcool.dissajobrecruiter.data.source.repository.application

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.ui.application.callback.DeleteApplicationByJobCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.UpdateApplicationMarkCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.UpdateApplicationStatusCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface ApplicationDataSource {
    fun getApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun getApplicationById(applicationId: String): LiveData<Resource<ApplicationEntity>>
    fun getAcceptedApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun getRejectedApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun getMarkedApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun getApplicationsByJob(jobId: String): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun updateApplicationMark(
        applicationId: String,
        isMarked: Boolean,
        callback: UpdateApplicationMarkCallback
    )

    fun updateApplicationStatus(
        applicationId: String,
        status: String,
        callback: UpdateApplicationStatusCallback
    )

    fun deleteApplicationsByJob(jobId: String, callback: DeleteApplicationByJobCallback)
}