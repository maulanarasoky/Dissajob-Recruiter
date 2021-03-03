package org.d3ifcool.dissajobrecruiter.data.entity

import com.google.firebase.database.Exclude

data class UserEntity(
    @get:Exclude
    var id: String? = "-",
    val firstName: String? = "-",
    val lastName: String? = "-",
    val address: String? = "-",
    val phoneNumber: String? = "-",
    val role: String? = "-",
    val imagePath: String? = "-"
)