package org.d3ifcool.dissajobrecruiter.utils

import com.google.firebase.database.*
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.job.JobPostCallback

object JobHelper {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("jobs")
    private val arrJob: MutableList<JobResponseEntity> = mutableListOf()

    fun createJob(job: JobDetailsResponseEntity, callback: JobPostCallback) {
        val id = database.push().key
        job.id = id.toString()
        job.postedBy = AuthHelper.currentUser?.uid.toString()
        database.child(job.id).setValue(job).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(it.message.toString())
        }
    }

    fun getJobs(callback: RemoteJobSource.LoadJobsCallback) {
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
}