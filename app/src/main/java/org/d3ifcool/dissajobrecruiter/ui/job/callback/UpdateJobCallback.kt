package org.d3ifcool.dissajobrecruiter.ui.job.callback

interface UpdateJobCallback {
    fun onSuccess()
    fun onFailure(messageId: Int)
}