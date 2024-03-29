package org.d3ifcool.dissajobrecruiter.data.source.repository.notification

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.notification.NotificationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalNotificationSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.notification.NotificationResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteNotificationSource
import org.d3ifcool.dissajobrecruiter.ui.notification.AddNotificationCallback
import org.d3ifcool.dissajobrecruiter.ui.notification.LoadNotificationsCallback
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.NetworkStateCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class NotificationRepository private constructor(
    private val remoteNotificationSource: RemoteNotificationSource,
    private val localNotificationSource: LocalNotificationSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    NotificationDataSource {

    companion object {
        @Volatile
        private var instance: NotificationRepository? = null

        fun getInstance(
            remoteNotification: RemoteNotificationSource,
            localNotification: LocalNotificationSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): NotificationRepository =
            instance ?: synchronized(this) {
                instance ?: NotificationRepository(
                    remoteNotification,
                    localNotification,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun getNotifications(recruiterId: String): LiveData<Resource<PagedList<NotificationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<NotificationEntity>, List<NotificationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<NotificationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localNotificationSource.getNotifications(recruiterId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<NotificationEntity>?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<List<NotificationResponseEntity>>> =
                remoteNotificationSource.getNotifications(
                    recruiterId,
                    object : LoadNotificationsCallback {
                        override fun onAllNotificationsReceived(notificationResponse: List<NotificationResponseEntity>): List<NotificationResponseEntity> {
                            return notificationResponse
                        }
                    })

            public override fun saveCallResult(data: List<NotificationResponseEntity>) {
                val notificationList = ArrayList<NotificationEntity>()
                for (response in data) {
                    val notification = NotificationEntity(
                        response.id,
                        response.jobId,
                        response.applicationId,
                        response.applicantId,
                        response.recruiterId,
                        response.notificationDate,
                    )
                    notificationList.add(notification)
                }

                localNotificationSource.insertNotification(notificationList)
            }
        }.asLiveData()
    }

    override fun addNotification(
        notificationData: NotificationResponseEntity,
        callback: AddNotificationCallback
    ) =
        appExecutors.diskIO()
            .execute { remoteNotificationSource.addNotification(notificationData, callback) }


}