package org.d3ifcool.dissajobrecruiter.data.source.local.entity.job

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "posted_by")
    var postedBy: String,

    @ColumnInfo(name = "posted_date")
    var postedDate: String,

    @ColumnInfo(name = "is_open")
    var isOpen: Boolean,

    @ColumnInfo(name = "is_open_for_disability")
    var isOpenForDisability: Boolean
)