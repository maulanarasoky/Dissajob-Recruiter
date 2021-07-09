package org.d3ifcool.dissajobrecruiter.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.callback.*

object JobHelper {

    private val jobDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("jobs")
    private val savedJobDatabase: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("saved_job")
    private val arrJob: MutableList<JobResponseEntity> = mutableListOf()

    fun createJob(job: JobDetailsResponseEntity, callbackCreate: CreateJobCallback) {
        val id = jobDatabase.push().key
        job.id = id.toString()
        jobDatabase.child(job.id).setValue(job).addOnSuccessListener {
            callbackCreate.onSuccess()
        }.addOnFailureListener {
            callbackCreate.onFailure(R.string.failure_alert_create_job)
        }
    }

    fun getJobs(recruiterId: String, callback: LoadJobsCallback) {
        jobDatabase.orderByChild("postedBy").equalTo(recruiterId)
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
                                data.child("open").value.toString().toBoolean(),
                                data.child("openForDisability").value.toString().toBoolean()
                            )
                            arrJob.add(job)
                        }
                    }
                    callback.onAllJobsReceived(arrJob)
                }

            })
    }

    fun getJobDetails(jobId: String, callback: LoadJobDetailsCallback) {
        jobDatabase.child(jobId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val jobDetails = JobDetailsResponseEntity(
                        dataSnapshot.key.toString(),
                        dataSnapshot.child("title").value.toString(),
                        dataSnapshot.child("description").value.toString(),
                        dataSnapshot.child("address").value.toString(),
                        dataSnapshot.child("qualification").value.toString(),
                        dataSnapshot.child("employment").value.toString(),
                        dataSnapshot.child("type").value.toString(),
                        dataSnapshot.child("industry").value.toString(),
                        dataSnapshot.child("salary").value.toString(),
                        dataSnapshot.child("postedBy").value.toString(),
                        dataSnapshot.child("postedDate").value.toString(),
                        dataSnapshot.child("updatedDate").value.toString(),
                        dataSnapshot.child("closedDate").value.toString(),
                        dataSnapshot.child("open").value.toString().toBoolean(),
                        dataSnapshot.child("openForDisability").value.toString().toBoolean(),
                        dataSnapshot.child("additionalInformation").value.toString()
                    )
                    callback.onJobDetailsReceived(jobDetails)
                }
            }

        })
    }

    fun updateJob(job: JobDetailsResponseEntity, callbackCreate: UpdateJobCallback) {
        jobDatabase.child(job.id).setValue(job).addOnSuccessListener {
            callbackCreate.onSuccess()
        }.addOnFailureListener {
            callbackCreate.onFailure(R.string.failure_alert_update_job)
        }
    }

    fun deleteJob(jobId: String, callback: DeleteJobCallback) {
        jobDatabase.child(jobId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.ref.removeValue().addOnSuccessListener {
                        callback.onSuccessDeleteJob()
                    }.addOnFailureListener {
                        callback.onFailureDeleteJob(R.string.failure_alert_delete_job)
                    }
                }
            }

        })
    }

    fun deleteSavedJobByJob(
        jobId: String,
        callback: DeleteSavedJobCallback
    ) {
        savedJobDatabase.orderByChild("jobId").equalTo(jobId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var index = 0
                        for (data in snapshot.children) {
                            val removeChild =
                                savedJobDatabase.child(data.key.toString()).removeValue()
                                    .addOnFailureListener {
                                        callback.onFailureDeleteSavedJob(R.string.failure_alert_delete_job)
                                    }

                            index++

                            if (index == snapshot.childrenCount.toInt()) {
                                removeChild.addOnSuccessListener {
                                    callback.onSuccessDeleteSavedJob()
                                }
                            }
                        }
                    } else {
                        callback.onSuccessDeleteSavedJob()
                    }
                }
            })
    }
}