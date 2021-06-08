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
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val applicantDetails = ApplicantResponseEntity(
                            dataSnapshot.key.toString(),
                            dataSnapshot.child("firstName").value.toString(),
                            dataSnapshot.child("lastName").value.toString(),
                            dataSnapshot.child("fullName").value.toString(),
                            dataSnapshot.child("email").value.toString(),
                            dataSnapshot.child("aboutMe").value.toString(),
                            dataSnapshot.child("phoneNumber").value.toString(),
                            dataSnapshot.child("imagePath").value.toString()
                        )
                        callback.onApplicantDetailsReceived(applicantDetails)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}