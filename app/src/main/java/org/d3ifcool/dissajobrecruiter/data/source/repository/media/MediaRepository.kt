package org.d3ifcool.dissajobrecruiter.data.source.repository.media

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalMediaSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.media.MediaResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteMediaSource
import org.d3ifcool.dissajobrecruiter.ui.applicant.media.LoadMediaDataCallback
import org.d3ifcool.dissajobrecruiter.ui.applicant.media.LoadMediaFileCallback
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.NetworkStateCallback
import org.d3ifcool.dissajobrecruiter.vo.Resource

class MediaRepository private constructor(
    private val remoteMediaSource: RemoteMediaSource,
    private val localMediaSource: LocalMediaSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    MediaDataSource {

    companion object {
        @Volatile
        private var instance: MediaRepository? = null

        fun getInstance(
            remoteMedia: RemoteMediaSource,
            localMedia: LocalMediaSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): MediaRepository =
            instance ?: synchronized(this) {
                instance ?: MediaRepository(
                    remoteMedia,
                    localMedia,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun getApplicantMedia(applicantId: String): LiveData<Resource<PagedList<MediaEntity>>> {
        return object :
            NetworkBoundResource<PagedList<MediaEntity>, List<MediaResponseEntity>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<MediaEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localMediaSource.getApplicantMedia(applicantId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<MediaEntity>?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<List<MediaResponseEntity>>> =
                remoteMediaSource.getApplicantMedia(applicantId, object : LoadMediaDataCallback {
                    override fun onAllMediaReceived(mediaResponse: List<MediaResponseEntity>): List<MediaResponseEntity> {
                        return mediaResponse
                    }
                })

            public override fun saveCallResult(data: List<MediaResponseEntity>) {
                val mediaList = ArrayList<MediaEntity>()
                for (response in data) {
                    val media = MediaEntity(
                        response.id,
                        response.mediaName,
                        response.mediaDescription,
                        response.applicantId,
                        response.fileId
                    )
                    mediaList.add(media)
                }
                localMediaSource.deleteAllApplicantMedia(applicantId)
                localMediaSource.insertMedia(mediaList)
            }
        }.asLiveData()
    }

    override fun getMediaById(fileId: String): LiveData<ByteArray> =
        remoteMediaSource.getMediaById(fileId, object : LoadMediaFileCallback {
            override fun onMediaFileReceived(mediaFile: ByteArray): ByteArray {
                return mediaFile
            }
        })
}