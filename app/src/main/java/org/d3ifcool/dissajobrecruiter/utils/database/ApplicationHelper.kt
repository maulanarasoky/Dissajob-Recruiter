package org.d3ifcool.dissajobrecruiter.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.application.callback.LoadAllApplicationsCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.LoadApplicationDataCallback
import org.d3ifcool.dissajobrecruiter.ui.application.callback.UpdateApplicationCallback

object ApplicationHelper {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("applications")
    private val arrApplication: MutableList<ApplicationResponseEntity> = mutableListOf()

    fun getAllApplications(callback: LoadAllApplicationsCallback) {
        database.orderByChild("applyDate")
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
                                data.child("marked").value.toString().toBoolean()
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
                        dataSnapshot.child("marked").value.toString().toBoolean()
                    )
                    callback.onApplicationDataReceived(application)
                }
            }

        })
    }

    fun getAllApplicationsByStatus(status: String, callback: LoadAllApplicationsCallback) {
        database.orderByChild("status").equalTo(status)
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
                                data.child("marked").value.toString().toBoolean()
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

    fun getMarkedApplications(callback: LoadAllApplicationsCallback) {
        database.child("applicants").orderByChild("is_marked").equalTo(true)
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
                                data.child("marked").value.toString().toBoolean()
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

    fun getApplicationsByJob(jobId: String, callback: LoadAllApplicationsCallback) {
        database.orderByChild("jobId").equalTo(jobId)
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
                                data.child("marked").value.toString().toBoolean()
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

    fun updateApplicationStatus(applicationId: String, status: String, callback: UpdateApplicationCallback) {
        database.child(applicationId).child("status").setValue(status).addOnSuccessListener {
            callback.onSuccessUpdate()
        }.addOnFailureListener {
            callback.onFailureUpdate(R.string.txt_error_occurred)
        }
    }
}