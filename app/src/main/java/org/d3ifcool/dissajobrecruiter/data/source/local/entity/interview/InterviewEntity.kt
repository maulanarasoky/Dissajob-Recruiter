package org.d3ifcool.dissajobrecruiter.data.source.local.entity.interview

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interview")
data class InterviewEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @NonNull
    @ColumnInfo(name = "application_id")
    var applicationId: String,

    @NonNull
    @ColumnInfo(name = "applicant_id")
    var applicantId: String,

    @ColumnInfo(name = "first_answer")
    var firstAnswer: String? = "-",

    @ColumnInfo(name = "second_answer")
    var secondAnswer: String? = "-",

    @ColumnInfo(name = "third_answer")
    var thirdAnswer: String? = "-"
)