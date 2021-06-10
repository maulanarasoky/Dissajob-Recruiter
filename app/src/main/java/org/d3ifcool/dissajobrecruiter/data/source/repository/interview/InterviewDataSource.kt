package org.d3ifcool.dissajobrecruiter.data.source.repository.interview

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobrecruiter.vo.Resource

interface InterviewDataSource {
    fun getInterviewAnswers(applicationId: String): LiveData<Resource<InterviewEntity>>
}