package org.d3ifcool.dissajobrecruiter.ui.application.callback

interface UpdateApplicationMarkCallback {
    fun onSuccessUpdateMark()
    fun onFailureUpdateMark(messageId: Int)
}