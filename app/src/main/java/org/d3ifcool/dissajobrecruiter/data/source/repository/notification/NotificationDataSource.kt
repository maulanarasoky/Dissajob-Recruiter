package org.d3ifcool.dissajobrecruiter.data.source.repository.notification

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.notification.NotificationEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.notification.NotificationResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.notification.AddNotificationCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface NotificationDataSource {
    fun getNotifications(recruiterId: String): LiveData<Resource<PagedList<NotificationEntity>>>
    fun addNotification(notificationData: NotificationResponseEntity, callback: AddNotificationCallback)
}