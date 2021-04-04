package org.d3ifcool.dissajobrecruiter.data.source.repository.notification

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.notification.NotificationEntity
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface NotificationDataSource {
    fun getNotifications(userId: String): LiveData<Resource<PagedList<NotificationEntity>>>
}