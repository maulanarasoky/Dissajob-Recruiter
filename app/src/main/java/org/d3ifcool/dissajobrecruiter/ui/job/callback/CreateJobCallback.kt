package org.d3ifcool.dissajobrecruiter.ui.job.callback

interface CreateJobCallback {
    fun onSuccess()
    fun onFailure(messageId: Int)
}