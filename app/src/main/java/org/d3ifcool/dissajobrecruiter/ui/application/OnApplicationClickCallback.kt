package org.d3ifcool.dissajobrecruiter.ui.application

interface OnApplicationClickCallback {
    fun onItemClick(applicationId: String, jobId: String, applicantId: String)
}