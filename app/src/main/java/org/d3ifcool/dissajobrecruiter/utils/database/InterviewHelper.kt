package org.d3ifcool.dissajobrecruiter.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.question.LoadInterviewAnswersCallback

object InterviewHelper {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("interview")

    fun getInterviewAnswers(applicationId: String, callback: LoadInterviewAnswersCallback) {
        database.orderByChild("applicationId").equalTo(applicationId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val interviewAnswer = InterviewResponseEntity(
                            dataSnapshot.key.toString(),
                            dataSnapshot.child("applicationId").value.toString(),
                            dataSnapshot.child("applicantId").value.toString(),
                            dataSnapshot.child("firstAnswer").value.toString(),
                            dataSnapshot.child("secondAnswer").value.toString(),
                            dataSnapshot.child("thirdAnswer").value.toString()
                        )
                        callback.onAllInterviewAnswersReceived(interviewAnswer)
                    }
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }
}