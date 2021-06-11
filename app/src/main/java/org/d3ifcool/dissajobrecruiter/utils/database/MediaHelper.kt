package org.d3ifcool.dissajobrecruiter.utils.database

import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.media.MediaResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.applicant.media.LoadMediaDataCallback
import org.d3ifcool.dissajobrecruiter.ui.applicant.media.LoadMediaFileCallback

object MediaHelper {
    private val storageRef = Firebase.storage.reference
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("media")
    private val ARR_MEDIA: MutableList<MediaResponseEntity> = mutableListOf()

    fun getApplicantMedia(applicantId: String, callback: LoadMediaDataCallback) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    ARR_MEDIA.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val mediaData = MediaResponseEntity(
                                data.key.toString(),
                                data.child("mediaName").value.toString(),
                                data.child("mediaDescription").value.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("fileId").value.toString()
                            )
                            ARR_MEDIA.add(mediaData)
                        }
                    }
                    callback.onAllMediaReceived(ARR_MEDIA)
                }
            })
    }

    fun getMediaById(fileId: String, callback: LoadMediaFileCallback) {
        storageRef.child("applicant/media/$fileId").getBytes(Long.MAX_VALUE)
            .addOnSuccessListener {
                callback.onMediaFileReceived(it)
            }
    }
}