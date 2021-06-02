package org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job

import com.google.firebase.database.Exclude

data class JobDetailsResponseEntity(
    @get:Exclude
    var id: String,
    val title: String,
    val description: String,
    val address: String,
    val qualification: String? = "-",
    val employment: String,
    val industry: String,
    val type: String,
    val salary: String? = "-",
    var postedBy: String,
    var postedDate: String,
    var updatedDate: String? = "-",
    var closedDate: String? = "-",
    var isOpen: Boolean,
    var isOpenForDisability: Boolean,
    var additionalInformation: String? = "-"
)