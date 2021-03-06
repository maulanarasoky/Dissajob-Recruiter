package org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant

import com.google.firebase.database.Exclude

data class ApplicantResponseEntity(
    @get:Exclude
    var id: String? = "-",
    var applicantId: String? = "-",
    var jobId: String? = "-",
    var applyDate: String? = "-",
    var status: String? = "-",
    var isMarked: Boolean? = false,
)

data class ApplicantDetailsResponseEntity(
    @get:Exclude
    var id: String? = "-",
    var firstName: String? = "-",
    var lastName: String? = "-",
    var aboutMe: String? = "-",
    var phoneNumber: String? = "-",
    var imagePath: String? = "-"
)