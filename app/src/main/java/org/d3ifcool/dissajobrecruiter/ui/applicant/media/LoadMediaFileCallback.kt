package org.d3ifcool.dissajobrecruiter.ui.applicant.media

interface LoadMediaFileCallback {
    fun onMediaFileReceived(mediaFile: ByteArray): ByteArray
}