package org.d3ifcool.dissajobrecruiter.ui.application.callback

interface UpdateApplicationCallback {
    fun onSuccessUpdate()
    fun onFailureUpdate(messageId: Int)
}