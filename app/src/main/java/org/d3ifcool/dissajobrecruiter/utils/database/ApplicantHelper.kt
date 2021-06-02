package org.d3ifcool.dissajobrecruiter.utils.database

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.applicant.LoadApplicantDetailsCallback

object ApplicantHelper {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users").child("applicants")

    fun getApplicantDetails(
        applicantId: String,
        callback: LoadApplicantDetailsCallback
    ) {
        database.child(applicantId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val applicant =
                            snapshot.getValue<ApplicantResponseEntity>() ?: return
                        applicant.id = snapshot.key ?: return
                        callback.onApplicantDetailsReceived(applicant)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}