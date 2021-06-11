package org.d3ifcool.dissajobrecruiter.ui.applicant.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.media.MediaRepository
import org.d3ifcool.dissajobrecruiter.vo.Resource

class MediaViewModel(private val mediaRepository: MediaRepository) : ViewModel() {
    fun getApplicantMedia(applicantId: String): LiveData<Resource<PagedList<MediaEntity>>> =
        mediaRepository.getApplicantMedia(applicantId)

    fun getMediaById(fileId: String): LiveData<ByteArray> =
        mediaRepository.getMediaById(fileId)
}