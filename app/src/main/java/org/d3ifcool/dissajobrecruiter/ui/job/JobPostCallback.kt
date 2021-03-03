package org.d3ifcool.dissajobrecruiter.ui.job

interface JobPostCallback {
    fun onSuccess()
    fun onFailure(message: String)
}