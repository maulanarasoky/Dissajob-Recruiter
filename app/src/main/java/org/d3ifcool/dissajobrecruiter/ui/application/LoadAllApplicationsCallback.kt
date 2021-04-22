package org.d3ifcool.dissajobrecruiter.ui.application

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.application.ApplicationResponseEntity

interface LoadAllApplicationsCallback {
    fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity>
}