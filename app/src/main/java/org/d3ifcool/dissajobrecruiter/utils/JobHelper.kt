package org.d3ifcool.dissajobrecruiter.utils

import com.google.firebase.database.*
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.callback.*

object JobHelper {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("jobs")
    private val arrJob: MutableList<JobResponseEntity> = mutableListOf()

    fun createJob(job: JobDetailsResponseEntity, callbackCreate: CreateJobCallback) {
        val id = database.push().key
        job.id = id.toString()
        job.postedBy = AuthHelper.currentUser?.uid.toString()
        database.child(job.id).setValue(job).addOnSuccessListener {
            callbackCreate.onSuccess()
        }.addOnFailureListener {
            callbackCreate.onFailure(R.string.failure_alert_create_job)
        }
    }

    fun getJobs(callback: LoadJobsCallback) {
        database.orderByChild("postedBy").equalTo(AuthHelper.currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrJob.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val job = JobResponseEntity(
                                data.key.toString(),
                                data.child("title").value.toString(),
                                data.child("description").value.toString(),
                                data.child("postedBy").value.toString(),
                                data.child("postedDate").value.toString(),
                                data.child("open").value.toString().toBoolean()
                            )
                            arrJob.add(job)
                        }
                    }
                    callback.onAllJobsReceived(arrJob)
                }

            })
    }

    fun getJobDetails(jobId: String, callback: LoadJobDetailsCallback) {
        database.child(jobId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val jobDetails = JobDetailsResponseEntity(
                        dataSnapshot.key.toString(),
                        dataSnapshot.child("title").value.toString(),
                        dataSnapshot.child("description").value.toString(),
                        dataSnapshot.child("qualification").value.toString(),
                        dataSnapshot.child("employment").value.toString(),
                        dataSnapshot.child("industry").value.toString(),
                        dataSnapshot.child("salary").value.toString().toInt(),
                        dataSnapshot.child("postedBy").value.toString(),
                        dataSnapshot.child("postedDate").value.toString(),
                        dataSnapshot.child("updatedDate").value.toString(),
                        dataSnapshot.child("closedDate").value.toString(),
                        dataSnapshot.child("open").value.toString().toBoolean()
                    )
                    callback.onJobDetailsReceived(jobDetails)
                }
            }

        })
    }

    fun updateJob(job: JobDetailsResponseEntity, callbackCreate: UpdateJobCallback) {
        job.postedBy = AuthHelper.currentUser?.uid.toString()
        database.child(job.id).setValue(job).addOnSuccessListener {
            callbackCreate.onSuccess()
        }.addOnFailureListener {
            callbackCreate.onFailure(R.string.failure_alert_update_job)
        }
    }

    fun deleteJob(jobId: String, callback: DeleteJobCallback) {
        database.child(jobId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.ref.removeValue().addOnSuccessListener {
                        callback.onDeleteSuccess()
                    }.addOnFailureListener {
                        callback.onDeleteFailure(R.string.failure_alert_delete_job)
                    }
                }
            }

        })
    }
}