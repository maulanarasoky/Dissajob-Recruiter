package org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity

import com.google.firebase.database.Exclude

data class JobResponseEntity(
    @get:Exclude
    var id: String,
    val title: String? = "-",
    val description: String? = "-",
    val postedBy: String? = "-",
    val postedDate: String? = "-",
    val imagePath: String? = "-",
    val isOpen: Boolean? = false
)