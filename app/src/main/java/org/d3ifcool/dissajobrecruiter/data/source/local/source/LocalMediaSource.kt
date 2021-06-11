package org.d3ifcool.dissajobrecruiter.data.source.local.source

import androidx.paging.DataSource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.room.MediaDao

class LocalMediaSource private constructor(
    private val mMediaDao: MediaDao
) {

    companion object {
        private var INSTANCE: LocalMediaSource? = null

        fun getInstance(mediaDao: MediaDao): LocalMediaSource =
            INSTANCE ?: LocalMediaSource(mediaDao)
    }

    fun getApplicantMedia(applicantId: String): DataSource.Factory<Int, MediaEntity> =
        mMediaDao.getApplicantMedia(applicantId)

    fun deleteAllApplicantMedia(applicantId: String) =
        mMediaDao.deleteAllApplicantMedia(applicantId)

    fun insertMedia(media: List<MediaEntity>) = mMediaDao.insertMedia(media)
}