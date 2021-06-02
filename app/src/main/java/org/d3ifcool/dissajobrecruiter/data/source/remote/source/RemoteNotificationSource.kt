package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.notification.NotificationResponseEntity
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.database.NotificationHelper

class RemoteNotificationSource private constructor(
    private val notificationHelper: NotificationHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteNotificationSource? = null

        fun getInstance(notificationHelper: NotificationHelper): RemoteNotificationSource =
            instance ?: synchronized(this) {
                instance ?: RemoteNotificationSource(notificationHelper)
            }
    }

    fun getNotifications(userId: String, callback: LoadNotificationsCallback): LiveData<ApiResponse<List<NotificationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultNotification = MutableLiveData<ApiResponse<List<NotificationResponseEntity>>>()
        notificationHelper.getNotifications(userId, object : LoadNotificationsCallback {
            override fun onAllNotificationsReceived(notificationResponse: List<NotificationResponseEntity>): List<NotificationResponseEntity> {
                resultNotification.value = ApiResponse.success(callback.onAllNotificationsReceived(notificationResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return notificationResponse
            }
        })
        return resultNotification
    }

    interface LoadNotificationsCallback {
        fun onAllNotificationsReceived(notificationResponse: List<NotificationResponseEntity>): List<NotificationResponseEntity>
    }
}