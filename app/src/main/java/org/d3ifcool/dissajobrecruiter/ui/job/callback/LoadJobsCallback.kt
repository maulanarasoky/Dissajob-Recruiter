package org.d3ifcool.dissajobrecruiter.ui.job.callback

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobResponseEntity

interface LoadJobsCallback {
    fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity>
}