package org.d3ifcool.dissajobrecruiter.ui.applicant

import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity

interface LoadApplicantDataCallback {
    fun onLoadApplicantDetailsCallback(applicantId: String, callback: LoadApplicantDataCallback)
    fun onGetApplicantDetails(applicantDetails: ApplicantEntity)
}