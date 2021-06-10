package org.d3ifcool.dissajobrecruiter.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobrecruiter.ui.question.LoadInterviewAnswersCallback
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.database.InterviewHelper

class RemoteInterviewSource private constructor(
    private val mInterviewHelper: InterviewHelper
) {
    companion object {
        @Volatile
        private var instance: RemoteInterviewSource? = null

        fun getInstance(interviewHelper: InterviewHelper): RemoteInterviewSource =
            instance ?: synchronized(this) {
                instance ?: RemoteInterviewSource(interviewHelper)
            }
    }

    fun getInterviewAnswers(
        applicationId: String,
        callback: LoadInterviewAnswersCallback
    ): LiveData<ApiResponse<InterviewResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultAnswer = MutableLiveData<ApiResponse<InterviewResponseEntity>>()
        mInterviewHelper.getInterviewAnswers(applicationId, object : LoadInterviewAnswersCallback {
            override fun onAllInterviewAnswersReceived(interviewAnswers: InterviewResponseEntity): InterviewResponseEntity {
                resultAnswer.value =
                    ApiResponse.success(callback.onAllInterviewAnswersReceived(interviewAnswers))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return interviewAnswers
            }
        })
        return resultAnswer
    }
}