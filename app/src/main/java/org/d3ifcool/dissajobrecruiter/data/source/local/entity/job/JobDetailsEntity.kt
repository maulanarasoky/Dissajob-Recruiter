package org.d3ifcool.dissajobrecruiter.data.source.local.entity.job

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "job_details")
data class JobDetailsEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "address")
    var address: String,

    @ColumnInfo(name = "qualification")
    var qualification: String? = "-",

    @ColumnInfo(name = "employment")
    var employment: String,

    @ColumnInfo(name = "type")
    var type: String,

    @ColumnInfo(name = "industry")
    var industry: String,

    @ColumnInfo(name = "salary")
    var salary: String? = "-",

    @ColumnInfo(name = "posted_by")
    var postedBy: String,

    @ColumnInfo(name = "posted_date")
    var postedDate: String,

    @ColumnInfo(name = "updated_date")
    var updatedDate: String? = "-",

    @ColumnInfo(name = "closed_date")
    var closedDate: String? = "-",

    @ColumnInfo(name = "is_open")
    var isOpen: Boolean,

    @ColumnInfo(name = "is_open_for_disability")
    var isOpenForDisability: Boolean,

    @ColumnInfo(name = "additional_information")
    var additionalInformation: String? = "-"
): Parcelable