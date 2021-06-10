package org.d3ifcool.dissajobrecruiter.ui.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.interview.InterviewRepository
import org.d3ifcool.dissajobrecruiter.vo.Resource

class InterviewViewModel(private val interviewRepository: InterviewRepository) : ViewModel() {
    fun getInterviewAnswers(applicationId: String): LiveData<Resource<InterviewEntity>> =
        interviewRepository.getInterviewAnswers(applicationId)
}