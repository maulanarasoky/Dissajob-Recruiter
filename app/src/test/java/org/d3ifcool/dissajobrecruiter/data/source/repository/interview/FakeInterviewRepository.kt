package org.d3ifcool.dissajobrecruiter.data.source.repository.interview

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobrecruiter.data.NetworkBoundResource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalInterviewSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.ApiResponse
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteInterviewSource
import org.d3ifcool.dissajobrecruiter.ui.question.LoadInterviewAnswersCallback
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.vo.Resource

class FakeInterviewRepository(
    private val remoteInterviewSource: RemoteInterviewSource,
    private val localInterviewSource: LocalInterviewSource,
    private val appExecutors: AppExecutors
) :
    InterviewDataSource {

    override fun getInterviewAnswers(applicationId: String): LiveData<Resource<InterviewEntity>> {
        return object :
            NetworkBoundResource<InterviewEntity, InterviewResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<InterviewEntity> =
                localInterviewSource.getInterviewAnswers(applicationId)

            override fun shouldFetch(data: InterviewEntity?): Boolean =
                data == null

            public override fun createCall(): LiveData<ApiResponse<InterviewResponseEntity>> =
                remoteInterviewSource.getInterviewAnswers(
                    applicationId,
                    object : LoadInterviewAnswersCallback {
                        override fun onAllInterviewAnswersReceived(interviewAnswers: InterviewResponseEntity): InterviewResponseEntity {
                            return interviewAnswers
                        }
                    })

            public override fun saveCallResult(data: InterviewResponseEntity) {
                val interview = InterviewEntity(
                    data.id,
                    data.applicationId,
                    data.applicantId,
                    data.firstAnswer,
                    data.secondAnswer,
                    data.thirdAnswer
                )
                localInterviewSource.insertInterviewAnswers(interview)
            }
        }.asLiveData()
    }
}