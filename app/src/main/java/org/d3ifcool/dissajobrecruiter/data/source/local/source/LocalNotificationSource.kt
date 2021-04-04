package org.d3ifcool.dissajobrecruiter.data.source.local.source

import androidx.paging.DataSource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.notification.NotificationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.room.JobDao
import org.d3ifcool.dissajobrecruiter.data.source.local.room.NotificationDao

class LocalNotificationSource private constructor(
    private val mNotificationDao: NotificationDao
) {

    companion object {
        private var INSTANCE: LocalNotificationSource? = null

        fun getInstance(notificationDao: NotificationDao): LocalNotificationSource =
            INSTANCE ?: LocalNotificationSource(notificationDao)
    }

    fun getNotifications(userId: String): DataSource.Factory<Int, NotificationEntity> = mNotificationDao.getNotifications(userId)

    fun insertNotification(notifications: List<NotificationEntity>) = mNotificationDao.insertNotification(notifications)

}