package org.d3ifcool.dissajobrecruiter.ui.job.callback

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity

interface LoadJobDetailsCallback {
    fun onJobDetailsReceived(jobResponse: JobDetailsResponseEntity): JobDetailsResponseEntity
}