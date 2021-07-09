package org.d3ifcool.dissajobrecruiter.ui.notification

interface OnNotificationClickCallback {
    fun onItemClick(applicationId: String, jobId: String, applicantId: String)
}