package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.notification.NotificationResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.notification.AddNotificationCallback
import org.d3ifcool.dissajobrecruiter.ui.notification.LoadNotificationsCallback
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

    fun getNotifications(
        recruiterId: String,
        callback: LoadNotificationsCallback
    ): LiveData<ApiResponse<List<NotificationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultNotification = MutableLiveData<ApiResponse<List<NotificationResponseEntity>>>()
        notificationHelper.getNotifications(recruiterId, object : LoadNotificationsCallback {
            override fun onAllNotificationsReceived(notificationResponse: List<NotificationResponseEntity>): List<NotificationResponseEntity> {
                resultNotification.value =
                    ApiResponse.success(callback.onAllNotificationsReceived(notificationResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return notificationResponse
            }
        })
        return resultNotification
    }

    fun addNotification(
        notificationData: NotificationResponseEntity,
        callback: AddNotificationCallback
    ) {
        EspressoIdlingResource.increment()
        notificationHelper.addNotification(notificationData, object : AddNotificationCallback {
            override fun onSuccessAddingNotification() {
                callback.onSuccessAddingNotification()
                EspressoIdlingResource.decrement()
            }

            override fun onFailureAddingNotification(messageId: Int) {
                callback.onFailureAddingNotification(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }
}