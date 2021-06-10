package org.d3ifcool.dissajobrecruiter.ui.question

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.interview.InterviewResponseEntity

interface LoadInterviewAnswersCallback {
    fun onAllInterviewAnswersReceived(interviewAnswers: InterviewResponseEntity): InterviewResponseEntity
}