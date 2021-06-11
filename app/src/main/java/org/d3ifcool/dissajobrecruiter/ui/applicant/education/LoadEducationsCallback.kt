package org.d3ifcool.dissajobrecruiter.ui.applicant.education

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.education.EducationResponseEntity

interface LoadEducationsCallback {
    fun onAllEducationsReceived(educationResponse: List<EducationResponseEntity>): List<EducationResponseEntity>
}