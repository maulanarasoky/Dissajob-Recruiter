package org.d3ifcool.dissajobrecruiter.data.source.local.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.media.MediaEntity

@Dao
interface MediaDao {
    @Query("SELECT * FROM media WHERE applicant_id = :applicantId")
    fun getApplicantMedia(applicantId: String): DataSource.Factory<Int, MediaEntity>

    @Query("DELETE FROM media WHERE applicant_id = :applicantId")
    fun deleteAllApplicantMedia(applicantId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedia(media: List<MediaEntity>)
}