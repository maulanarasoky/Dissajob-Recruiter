package org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job

import com.google.firebase.database.Exclude

data class JobResponseEntity(
    @get:Exclude
    var id: String,
    var title: String,
    var description: String,
    var postedBy: String,
    var postedDate: String,
    var isOpen: Boolean,
    var isOpenForDisability: Boolean
)