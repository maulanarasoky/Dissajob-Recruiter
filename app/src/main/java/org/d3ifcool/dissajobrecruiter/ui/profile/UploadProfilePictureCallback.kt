package org.d3ifcool.dissajobrecruiter.ui.profile

interface UploadProfilePictureCallback {
    fun onSuccessUpload(imageId: String)
    fun onFailureUpload(messageId: Int)
}