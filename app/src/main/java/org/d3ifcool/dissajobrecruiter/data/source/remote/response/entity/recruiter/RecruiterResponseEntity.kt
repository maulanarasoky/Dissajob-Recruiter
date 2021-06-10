package org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter

import com.google.firebase.database.Exclude

data class RecruiterResponseEntity(
    @get:Exclude
    var id: String? = "-",
    val firstName: String? = "-",
    val lastName: String? = "-",
    val fullName: String? = "-",
    val email: String? = "-",
    val address: String? = "-",
    val phoneNumber: String? = "-",
    val imagePath: String? = "-"
)