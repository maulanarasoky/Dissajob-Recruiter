package org.d3ifcool.dissajobrecruiter.data.source.repository.media

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface MediaDataSource {
    fun getApplicantMedia(applicantId: String): LiveData<Resource<PagedList<MediaEntity>>>
    fun getMediaById(fileId: String): LiveData<ByteArray>
}