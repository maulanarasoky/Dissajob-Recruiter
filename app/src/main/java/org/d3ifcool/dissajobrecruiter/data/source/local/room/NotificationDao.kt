package org.d3ifcool.dissajobrecruiter.data.source.local.room

import androidx.paging.DataSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.notification.NotificationEntity

interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE recruiter_id = :userId")
    fun getNotifications(userId: String): DataSource.Factory<Int, NotificationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notifications: List<NotificationEntity>)
}