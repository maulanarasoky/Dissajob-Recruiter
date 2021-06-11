package org.d3ifcool.dissajobrecruiter.ui.applicant.media

interface LoadPdfCallback {
    fun onLoadPdfData(mediaId: String, callback: LoadPdfCallback)
    fun onPdfDataReceived(mediaFile: ByteArray)
}