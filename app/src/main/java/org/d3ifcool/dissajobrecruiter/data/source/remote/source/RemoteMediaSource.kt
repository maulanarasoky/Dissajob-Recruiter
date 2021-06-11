package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.media.MediaResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.applicant.media.LoadMediaDataCallback
import org.d3ifcool.dissajobrecruiter.ui.applicant.media.LoadMediaFileCallback
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.database.MediaHelper

class RemoteMediaSource private constructor(
    private val mediaHelper: MediaHelper
) {
    companion object {
        @Volatile
        private var instance: RemoteMediaSource? = null

        fun getInstance(mediaHelper: MediaHelper): RemoteMediaSource =
            instance ?: synchronized(this) {
                instance ?: RemoteMediaSource(mediaHelper)
            }
    }

    fun getApplicantMedia(
        applicantId: String,
        callback: LoadMediaDataCallback
    ): LiveData<ApiResponse<List<MediaResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultMedia = MutableLiveData<ApiResponse<List<MediaResponseEntity>>>()
        mediaHelper.getApplicantMedia(applicantId, object : LoadMediaDataCallback {
            override fun onAllMediaReceived(mediaResponse: List<MediaResponseEntity>): List<MediaResponseEntity> {
                resultMedia.value =
                    ApiResponse.success(callback.onAllMediaReceived(mediaResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return mediaResponse
            }
        })
        return resultMedia
    }

    fun getMediaById(
        fileId: String,
        callback: LoadMediaFileCallback
    ): LiveData<ByteArray> {
        EspressoIdlingResource.increment()
        val resultMedia = MutableLiveData<ByteArray>()
        mediaHelper.getMediaById(fileId, object : LoadMediaFileCallback {
            override fun onMediaFileReceived(mediaFile: ByteArray): ByteArray {
                resultMedia.value = callback.onMediaFileReceived(mediaFile)
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return mediaFile
            }
        })
        return resultMedia
    }
}