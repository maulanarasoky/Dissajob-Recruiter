package org.d3ifcool.dissajobrecruiter.ui.application.callback

interface UpdateApplicationStatusCallback {
    fun onSuccessUpdateStatus()
    fun onFailureUpdateStatus(messageId: Int)
}