package org.d3ifcool.dissajobrecruiter.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.JobEntity
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface JobDataSource {
    fun getJobs(): LiveData<Resource<PagedList<JobEntity>>>
}