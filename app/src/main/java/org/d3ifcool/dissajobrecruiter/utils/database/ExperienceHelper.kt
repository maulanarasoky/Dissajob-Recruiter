package org.d3ifcool.dissajobrecruiter.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.experience.ExperienceResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.applicant.experience.LoadExperienceCallback

object ExperienceHelper {
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("experiences")
    private val arrExperience: MutableList<ExperienceResponseEntity> = mutableListOf()

    fun getApplicantExperiences(applicantId: String, callback: LoadExperienceCallback) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrExperience.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val experience = ExperienceResponseEntity(
                                data.key.toString(),
                                data.child("title").value.toString(),
                                data.child("employmentType").value.toString(),
                                data.child("companyName").value.toString(),
                                data.child("location").value.toString(),
                                data.child("startMonth").value.toString().toInt(),
                                data.child("startYear").value.toString().toInt(),
                                data.child("endMonth").value.toString().toInt(),
                                data.child("endYear").value.toString().toInt(),
                                data.child("description").value.toString(),
                                data.child("currentlyWorking").value.toString().toBoolean(),
                                data.child("applicantId").value.toString()
                            )
                            arrExperience.add(experience)
                        }
                    }
                    callback.onAllExperiencesReceived(arrExperience)
                }

            })
    }
}