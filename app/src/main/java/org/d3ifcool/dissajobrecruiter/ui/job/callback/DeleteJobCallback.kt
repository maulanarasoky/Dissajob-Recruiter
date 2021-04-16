package org.d3ifcool.dissajobrecruiter.ui.job.callback

interface DeleteJobCallback {
    fun onDeleteSuccess()
    fun onDeleteFailure(messageId: Int)
}