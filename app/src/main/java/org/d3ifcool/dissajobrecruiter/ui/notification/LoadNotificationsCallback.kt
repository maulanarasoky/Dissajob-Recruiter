package org.d3ifcool.dissajobrecruiter.ui.notification

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.notification.NotificationResponseEntity

interface LoadNotificationsCallback {
    fun onAllNotificationsReceived(notificationResponse: List<NotificationResponseEntity>): List<NotificationResponseEntity>
}