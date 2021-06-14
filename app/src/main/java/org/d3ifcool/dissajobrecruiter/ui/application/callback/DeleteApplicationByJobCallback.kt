package org.d3ifcool.dissajobrecruiter.ui.application.callback

interface DeleteApplicationByJobCallback {
    fun onSuccessDeleteApplications()
    fun onFailureDeleteApplications(messageId: Int)
}