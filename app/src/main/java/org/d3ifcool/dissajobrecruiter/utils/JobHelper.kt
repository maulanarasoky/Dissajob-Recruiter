package org.d3ifcool.dissajobrecruiter.utils

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import org.d3ifcool.dissajobrecruiter.data.source.remote.RemoteDataSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.JobResponseEntity

object JobHelper {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("jobs")
    private val arrJob: MutableList<JobResponseEntity> = mutableListOf()

    fun getJobs(callback: RemoteDataSource.LoadJobsCallback) {
        database.orderByChild("posted")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrJob.clear()
                    for (data in dataSnapshot.children.reversed()) {
                        val job = data.getValue<JobResponseEntity>() ?: return
                        job.id = data.key ?: return
                        arrJob.add(job)
                    }

                    callback.onAllJobsReceived(arrJob)
                }

            })
    }
}