package org.d3ifcool.dissajobrecruiter.ui.job.callback

interface DeleteJobCallback {
    fun onSuccessDeleteJob()
    fun onFailureDeleteJob(messageId: Int)
}