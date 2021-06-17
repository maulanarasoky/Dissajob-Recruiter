package org.d3ifcool.dissajobrecruiter.data.source.repository.application

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalApplicationSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteApplicationSource
import org.d3ifcool.dissajobrecruiter.ui.application.callback.*
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.vo.Resource

class FakeApplicationRepository(
    private val remoteApplicationSource: RemoteApplicationSource,
    private val localApplicationSource: LocalApplicationSource,
    private val appExecutors: AppExecutors
) :
    ApplicationDataSource {

    override fun getApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicationEntity>, List<ApplicationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localApplicationSource.getApplications(recruiterId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<ApplicationEntity>?): Boolean =
                data == null

            public override fun createCall(): LiveData<ApiResponse<List<ApplicationResponseEntity>>> =
                remoteApplicationSource.getAllApplications(
                    recruiterId,
                    object : LoadAllApplicationsCallback {
                        override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                            return applicationsResponse
                        }
                    })

            public override fun saveCallResult(data: List<ApplicationResponseEntity>) {
                localApplicationSource.deleteAllApplications()
                val applicationList = ArrayList<ApplicationEntity>()
                for (response in data) {
                    val application = ApplicationEntity(
                        response.id,
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.updatedDate,
                        response.status,
                        response.isMarked,
                        response.recruiterId
                    )
                    applicationList.add(application)
                }
                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun getApplicationById(applicationId: String): LiveData<Resource<ApplicationEntity>> {
        return object :
            NetworkBoundResource<ApplicationEntity, ApplicationResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<ApplicationEntity> =
                localApplicationSource.getApplicationById(applicationId)

            override fun shouldFetch(data: ApplicationEntity?): Boolean =
                data == null

            public override fun createCall(): LiveData<ApiResponse<ApplicationResponseEntity>> =
                remoteApplicationSource.getApplicationById(
                    applicationId,
                    object : LoadApplicationDataCallback {
                        override fun onApplicationDataReceived(applicationResponse: ApplicationResponseEntity): ApplicationResponseEntity {
                            return applicationResponse
                        }
                    })

            public override fun saveCallResult(data: ApplicationResponseEntity) {
                val applicationList = ArrayList<ApplicationEntity>()
                val application = ApplicationEntity(
                    data.id,
                    data.applicantId,
                    data.jobId,
                    data.applyDate,
                    data.updatedDate,
                    data.status,
                    data.isMarked,
                    data.recruiterId
                )
                applicationList.add(application)
                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun getAcceptedApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicationEntity>, List<ApplicationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localApplicationSource.getAcceptedApplications(recruiterId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<ApplicationEntity>?): Boolean =
                data == null

            public override fun createCall(): LiveData<ApiResponse<List<ApplicationResponseEntity>>> =
                remoteApplicationSource.getAcceptedApplications(
                    recruiterId,
                    object : LoadAllApplicationsCallback {
                        override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                            return applicationsResponse
                        }
                    })

            public override fun saveCallResult(data: List<ApplicationResponseEntity>) {
                val applicationList = ArrayList<ApplicationEntity>()
                for (response in data) {
                    val application = ApplicationEntity(
                        response.id,
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.updatedDate,
                        response.status,
                        response.isMarked,
                        response.recruiterId
                    )
                    applicationList.add(application)
                }
                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun getRejectedApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicationEntity>, List<ApplicationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localApplicationSource.getRejectedApplications(recruiterId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<ApplicationEntity>?): Boolean =
                data == null

            public override fun createCall(): LiveData<ApiResponse<List<ApplicationResponseEntity>>> =
                remoteApplicationSource.getRejectedApplications(recruiterId, object :
                    LoadAllApplicationsCallback {
                    override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                        return applicationsResponse
                    }
                })

            public override fun saveCallResult(data: List<ApplicationResponseEntity>) {
                val applicationList = ArrayList<ApplicationEntity>()
                for (response in data) {
                    val application = ApplicationEntity(
                        response.id,
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.updatedDate,
                        response.status,
                        response.isMarked,
                        response.recruiterId
                    )
                    applicationList.add(application)
                }
                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun getMarkedApplications(recruiterId: String): LiveData<Resource<PagedList<ApplicationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicationEntity>, List<ApplicationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localApplicationSource.getMarkedApplications(recruiterId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<ApplicationEntity>?): Boolean =
                data == null

            public override fun createCall(): LiveData<ApiResponse<List<ApplicationResponseEntity>>> =
                remoteApplicationSource.getMarkedApplications(
                    recruiterId,
                    object : LoadAllApplicationsCallback {
                        override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                            return applicationsResponse
                        }
                    })

            public override fun saveCallResult(data: List<ApplicationResponseEntity>) {
                val applicationList = ArrayList<ApplicationEntity>()
                for (response in data) {
                    val application = ApplicationEntity(
                        response.id,
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.updatedDate,
                        response.status,
                        response.isMarked,
                        response.recruiterId
                    )
                    applicationList.add(application)
                }
                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun getApplicationsByJob(jobId: String): LiveData<Resource<PagedList<ApplicationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicationEntity>, List<ApplicationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localApplicationSource.getApplicationsByJob(jobId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<ApplicationEntity>?): Boolean =
                data == null

            public override fun createCall(): LiveData<ApiResponse<List<ApplicationResponseEntity>>> =
                remoteApplicationSource.getApplicationsByJob(
                    jobId,
                    object : LoadAllApplicationsCallback {
                        override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                            return applicationsResponse
                        }
                    })

            public override fun saveCallResult(data: List<ApplicationResponseEntity>) {
                val applicationList = ArrayList<ApplicationEntity>()
                for (response in data) {
                    val application = ApplicationEntity(
                        response.id,
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.updatedDate,
                        response.status,
                        response.isMarked,
                        response.recruiterId
                    )
                    applicationList.add(application)
                }
                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun updateApplicationMark(
        applicationId: String,
        isMarked: Boolean,
        callback: UpdateApplicationMarkCallback
    ) = appExecutors.diskIO().execute {
        localApplicationSource.updateApplicationMark(applicationId, isMarked)
        remoteApplicationSource.updateApplicationMark(applicationId, isMarked, callback)
    }

    override fun updateApplicationStatus(
        applicationId: String,
        status: String,
        callback: UpdateApplicationStatusCallback
    ) = appExecutors.diskIO().execute {
        localApplicationSource.updateApplicationStatus(applicationId, status)
        remoteApplicationSource.updateApplicationStatus(applicationId, status, callback)
    }

    override fun deleteApplicationsByJob(jobId: String, callback: DeleteApplicationByJobCallback) =
        appExecutors.diskIO().execute {
            localApplicationSource.deleteApplicationsByJob(jobId)
            remoteApplicationSource.deleteApplicationsByJob(jobId, callback)
        }
}