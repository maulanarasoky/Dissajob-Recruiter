package org.d3ifcool.dissajobrecruiter.utils

import com.google.firebase.database.*
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.notification.NotificationResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteNotificationSource

object NotificationHelper {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("recruiter_notifications")
    private val arrNotification: MutableList<NotificationResponseEntity> = mutableListOf()

    fun getNotifications(userId: String, callback: RemoteNotificationSource.LoadNotificationsCallback) {
        database.orderByChild("recruiterId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrNotification.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val notification = NotificationResponseEntity(
                                data.key.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("recruiterId").value.toString(),
                                data.child("notificationDate").value.toString(),
                            )
                            arrNotification.add(notification)
                        }
                    }
                    callback.onAllNotificationsReceived(arrNotification)
                }

            })
    }
}