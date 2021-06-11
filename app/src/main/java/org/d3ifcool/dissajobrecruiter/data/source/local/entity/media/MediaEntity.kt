package org.d3ifcool.dissajobrecruiter.data.source.local.entity.media

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media")
data class MediaEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "media_name")
    var mediaName: String,

    @ColumnInfo(name = "media_description")
    var mediaDescription: String? = "-",

    @NonNull
    @ColumnInfo(name = "applicant_id")
    var applicantId: String,

    @NonNull
    @ColumnInfo(name = "file_id")
    var fileId: String
)