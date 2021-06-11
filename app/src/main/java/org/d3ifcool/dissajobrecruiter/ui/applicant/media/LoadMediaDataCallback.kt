package org.d3ifcool.dissajobrecruiter.ui.applicant.media

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.media.MediaResponseEntity

interface LoadMediaDataCallback {
    fun onAllMediaReceived(mediaResponse: List<MediaResponseEntity>): List<MediaResponseEntity>
}