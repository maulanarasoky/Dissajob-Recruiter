package org.d3ifcool.dissajobrecruiter.ui.job.callback

interface DeleteSavedJobCallback {
    fun onSuccessDeleteSavedJob()
    fun onFailureDeleteSavedJob(messageId: Int)
}