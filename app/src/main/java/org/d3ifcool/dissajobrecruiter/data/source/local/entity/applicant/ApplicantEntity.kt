package org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude

@Entity(tableName = "applicants")
data class ApplicantEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @NonNull
    @ColumnInfo(name = "applicant_id")
    var applicantId: String? = "-",

    @NonNull
    @ColumnInfo(name = "job_id")
    var jobId: String? = "-",

    @ColumnInfo(name = "apply_date")
    var applyDate: String? = "-",

    @ColumnInfo(name = "status")
    var status: String? = "-",

    @ColumnInfo(name = "is_marked")
    var isMarked: Boolean? = false
)

@Entity(tableName = "applicant_details")
data class ApplicantDetailsEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "first_name")
    var firstName: String? = "-",

    @ColumnInfo(name = "last_name")
    var lastName: String? = "-",

    @ColumnInfo(name = "about_me")
    var aboutMe: String? = "-",

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String? = "-",

    @ColumnInfo(name = "image_path")
    var imagePath: String? = "-"
)