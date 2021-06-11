package org.d3ifcool.dissajobrecruiter.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.applicant.education.LoadEducationsCallback

object EducationHelper {
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("educations")
    private val arrEducation: MutableList<EducationResponseEntity> = mutableListOf()

    fun getApplicantEducations(applicantId: String, callback: LoadEducationsCallback) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrEducation.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val education = EducationResponseEntity(
                                data.key.toString(),
                                data.child("schoolName").value.toString(),
                                data.child("educationLevel").value.toString(),
                                data.child("fieldOfStudy").value.toString(),
                                data.child("startMonth").value.toString().toInt(),
                                data.child("startYear").value.toString().toInt(),
                                data.child("endMonth").value.toString().toInt(),
                                data.child("endYear").value.toString().toInt(),
                                data.child("description").value.toString(),
                                data.child("applicantId").value.toString()
                            )
                            arrEducation.add(education)
                        }
                    }
                    callback.onAllEducationsReceived(arrEducation)
                }

            })
    }
}