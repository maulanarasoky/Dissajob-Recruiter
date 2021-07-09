package org.d3ifcool.dissajobrecruiter.ui.job.callback

import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity

interface LoadJobDataCallback {
    fun onLoadJobDetailsCallback(jobId: String, callback: LoadJobDataCallback)
    fun onGetJobDetails(jobDetails: JobDetailsEntity)
}