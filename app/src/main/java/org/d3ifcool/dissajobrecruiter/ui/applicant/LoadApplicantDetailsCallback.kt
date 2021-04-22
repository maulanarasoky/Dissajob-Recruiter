package org.d3ifcool.dissajobrecruiter.ui.applicant

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant.ApplicantResponseEntity

interface LoadApplicantDetailsCallback {
    fun onApplicantDetailsReceived(applicantDetailsResponse: ApplicantResponseEntity): ApplicantResponseEntity
}