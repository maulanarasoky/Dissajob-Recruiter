package org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity

import com.google.firebase.database.Exclude

data class UserResponseEntity(
    @get:Exclude
    var id: String? = "-",
    val firstName: String? = "-",
    val lastName: String? = "-",
    val address: String? = "-",
    val phoneNumber: String? = "-",
    val role: String? = "-",
    val imagePath: String? = "-"
)