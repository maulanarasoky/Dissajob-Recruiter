package org.d3ifcool.dissajobrecruiter.utils.database

import android.util.Log
import com.google.firebase.database.*
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.application.callback.*
import org.d3ifcool.dissajobrecruiter.utils.DateUtils

object ApplicationHelper {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("applications")
    private val arrApplication: MutableList<ApplicationResponseEntity> = mutableListOf()

    fun getAllApplications(recruiterId: String, callback: LoadAllApplicationsCallback) {
        database.orderByChild("recruiterId").equalTo(recruiterId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrApplication.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val application = ApplicationResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
                                data.child("updatedDate").value.toString(),
                                data.child("status").value.toString(),
                                data.child("marked").value.toString().toBoolean(),
                                data.child("recruiterId").value.toString()
                            )
                            arrApplication.add(application)
                        }
                        callback.onAllApplicationsReceived(arrApplication)
                    }
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun getApplicationById(applicationId: String, callback: LoadApplicationDataCallback) {
        database.child(applicationId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val application = ApplicationResponseEntity(
                        dataSnapshot.key.toString(),
                        dataSnapshot.child("applicantId").value.toString(),
                        dataSnapshot.child("jobId").value.toString(),
                        dataSnapshot.child("applyDate").value.toString(),
                        dataSnapshot.child("updatedDate").value.toString(),
                        dataSnapshot.child("status").value.toString(),
                        dataSnapshot.child("marked").value.toString().toBoolean(),
                        dataSnapshot.child("recruiterId").value.toString()
                    )
                    callback.onApplicationDataReceived(application)
                }
            }

        })
    }

    fun getAllApplicationsByStatus(
        recruiterId: String,
        status: String,
        callback: LoadAllApplicationsCallback
    ) {
        database.orderByChild("recruiterId").equalTo(recruiterId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrApplication.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            if (data.child("status").value.toString() == status) {
                                val application = ApplicationResponseEntity(
                                    data.key.toString(),
                                    data.child("applicantId").value.toString(),
                                    data.child("jobId").value.toString(),
                                    data.child("applyDate").value.toString(),
                                    data.child("updatedDate").value.toString(),
                                    data.child("status").value.toString(),
                                    data.child("marked").value.toString().toBoolean(),
                                    data.child("recruiterId").value.toString()
                                )
                                arrApplication.add(application)
                            }
                        }
                    }
                    callback.onAllApplicationsReceived(arrApplication)
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun getMarkedApplications(recruiterId: String, callback: LoadAllApplicationsCallback) {
        database.child("recruiterId").equalTo(recruiterId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrApplication.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            if (data.child("marked").value.toString().toBoolean()) {
                                val application = ApplicationResponseEntity(
                                    data.key.toString(),
                                    data.child("applicantId").value.toString(),
                                    data.child("jobId").value.toString(),
                                    data.child("applyDate").value.toString(),
                                    data.child("updatedDate").value.toString(),
                                    data.child("status").value.toString(),
                                    data.child("marked").value.toString().toBoolean(),
                                    data.child("recruiterId").value.toString()
                                )
                                arrApplication.add(application)
                            }
                        }
                    }
                    callback.onAllApplicationsReceived(arrApplication)
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun getApplicationsByJob(jobId: String, callback: LoadAllApplicationsCallback) {
        database.orderByChild("jobId").equalTo(jobId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrApplication.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val application = ApplicationResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
                                data.child("updatedDate").value.toString(),
                                data.child("status").value.toString(),
                                data.child("marked").value.toString().toBoolean(),
                                data.child("recruiterId").value.toString()
                            )
                            arrApplication.add(application)
                        }
                    }
                    callback.onAllApplicationsReceived(arrApplication)
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun updateApplicationMark(
        applicationId: String,
        isMarked: Boolean,
        callback: UpdateApplicationMarkCallback
    ) {
        database.child(applicationId).child("marked").setValue(isMarked).addOnSuccessListener {
            callback.onSuccessUpdateMark()
        }.addOnFailureListener {
            callback.onFailureUpdateMark(R.string.txt_error_occurred)
        }
    }

    fun updateApplicationStatus(
        applicationId: String,
        status: String,
        callback: UpdateApplicationStatusCallback
    ) {
        database.child(applicationId).child("status").setValue(status).addOnSuccessListener {
            updateApplicationUpdatedDate(applicationId, callback)
        }.addOnFailureListener {
            callback.onFailureUpdateStatus(R.string.txt_error_occurred)
        }
    }

    private fun updateApplicationUpdatedDate(
        applicationId: String,
        callback: UpdateApplicationStatusCallback
    ) {
        database.child(applicationId).child("updatedDate").setValue(DateUtils.getCurrentDate())
            .addOnSuccessListener {
                callback.onSuccessUpdateStatus()
            }.addOnFailureListener {
                callback.onFailureUpdateStatus(R.string.txt_error_occurred)
            }
    }

    fun deleteApplicationsByJob(
        jobId: String,
        callback: DeleteApplicationByJobCallback
    ) {
        database.orderByChild("jobId").equalTo(jobId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var index = 0
                        for (data in snapshot.children) {
                            val removeChild = database.child(data.key.toString()).removeValue()
                                .addOnFailureListener {
                                    callback.onFailureDeleteApplications(R.string.failure_alert_delete_job)
                                }

                            index++

                            if (index == snapshot.childrenCount.toInt()) {
                                removeChild.addOnSuccessListener {
                                    callback.onSuccessDeleteApplications()
                                }
                            }
                        }
                    } else {
                        callback.onSuccessDeleteApplications()
                    }
                }
            })
    }
}