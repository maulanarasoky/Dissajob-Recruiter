package org.d3ifcool.dissajobrecruiter.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.notification.NotificationEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.notification.NotificationResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.notification.NotificationRepository
import org.d3ifcool.dissajobrecruiter.vo.Resource

class NotificationViewModel(private val notificationRepository: NotificationRepository) :
    ViewModel() {
    fun getNotifications(recruiterId: String): LiveData<Resource<PagedList<NotificationEntity>>> =
        notificationRepository.getNotifications(recruiterId)

    fun addNotification(
        notificationData: NotificationResponseEntity,
        callback: AddNotificationCallback
    ) = notificationRepository.addNotification(notificationData, callback)
}