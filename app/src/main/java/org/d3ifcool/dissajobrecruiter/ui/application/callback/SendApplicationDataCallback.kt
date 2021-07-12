package org.d3ifcool.dissajobrecruiter.ui.application.callback

import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity

interface SendApplicationDataCallback {
    fun sendApplicationAndApplicantData(applicationEntity: ApplicationEntity, applicantEntity: ApplicantEntity, jobDetailsEntity: JobDetailsEntity)
}