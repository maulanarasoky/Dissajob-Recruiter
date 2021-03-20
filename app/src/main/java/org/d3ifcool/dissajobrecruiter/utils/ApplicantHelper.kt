package org.d3ifcool.dissajobrecruiter.utils

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant.ApplicantDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteApplicantSource

object ApplicantHelper {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().reference
    private val arrApplicant: MutableList<ApplicantResponseEntity> = mutableListOf()
    private val arrApplicantDetails: MutableList<ApplicantDetailsResponseEntity> = mutableListOf()

    fun getAllApplicants(callback: RemoteApplicantSource.LoadAllApplicantsCallback) {
        database.child("applicants").orderByChild("applyDate")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrApplicant.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val job = ApplicantResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
                                data.child("status").value.toString(),
                                data.child("marked").value.toString().toBoolean()
                            )
                            arrApplicant.add(job)
                        }
                    }
                    callback.onAllApplicantsReceived(arrApplicant)
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun getAllApplicantsByStatus(status: String, callback: RemoteApplicantSource.LoadAllApplicantsCallback) {
        database.child("applicants").orderByChild("status").equalTo(status)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrApplicant.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val job = ApplicantResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
                                data.child("status").value.toString(),
                                data.child("marked").value.toString().toBoolean()
                            )
                            arrApplicant.add(job)
                        }
                    }
                    callback.onAllApplicantsReceived(arrApplicant)
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun getMarkedApplicants(callback: RemoteApplicantSource.LoadAllApplicantsCallback) {
        database.child("applicants").orderByChild("is_marked").equalTo(true)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrApplicant.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val job = ApplicantResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
                                data.child("status").value.toString(),
                                data.child("marked").value.toString().toBoolean()
                            )
                            arrApplicant.add(job)
                        }
                    }
                    callback.onAllApplicantsReceived(arrApplicant)
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun getApplicantDetails(
        applicantId: String,
        callback: RemoteApplicantSource.LoadApplicantDetailsCallback
    ) {
        database.child("users").child("applicants").child(applicantId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val applicantDetails =
                            snapshot.getValue<ApplicantDetailsResponseEntity>() ?: return
                        applicantDetails.id = snapshot.key ?: return
                        callback.onApplicantDetailsReceived(applicantDetails)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}