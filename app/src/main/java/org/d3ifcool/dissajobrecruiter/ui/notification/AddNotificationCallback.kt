package org.d3ifcool.dissajobrecruiter.ui.notification

interface AddNotificationCallback {
    fun onSuccessAddingNotification()
    fun onFailureAddingNotification(messageId: Int)
}