package org.d3ifcool.dissajobrecruiter.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude

data class JobEntity(
    @get:Exclude
    var id: String,
    val title: String? = "-",
    val description: String? = "-",
    val postedBy: String? = "-",
    val postedDate: String? = "-",
    val imagePath: String? = "-",
    val isOpen: Boolean? = false
)

data class JobDetailsEntity(
    @get:Exclude
    var id: String,
    val title: String? = "-",
    val description: String? = "-",
    val qualification: String? = "-",
    val employment: String? = "-",
    val industry: String? = "-",
    val salary: Int? = 0,
    var postedBy: String? = "-",
    val postedDate: String? = "-",
    val updatedDate: String? = "-",
    val closedDate: String? = "-",
    val imagePath: String? = "-",
    val isOpen: Boolean? = false
)