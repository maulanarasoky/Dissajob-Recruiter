package org.d3ifcool.dissajobrecruiter.data.source.repository.job

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalJobSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.PagedListUtil
import org.d3ifcool.dissajobrecruiter.utils.dummy.JobDummy
import org.d3ifcool.dissajobrecruiter.utils.dummy.RecruiterDummy
import org.d3ifcool.dissajobrecruiter.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class JobRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteJobSource::class.java)
    private val local = mock(LocalJobSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val jobRepository = FakeJobRepository(remote, local, appExecutors)

    private val jobResponse = JobDummy.generateJobsData()
    private val jobDetailsResponse = JobDummy.generateJobDetails()

    private val recruiterData = RecruiterDummy.generateRecruiterDetails()

    @Test
    fun getJobsTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, JobEntity>
        `when`(local.getJobs(recruiterData.id)).thenReturn(dataSourceFactory)
        jobRepository.getJobs(recruiterData.id)

        val jobEntities = Resource.success(PagedListUtil.mockPagedList(JobDummy.generateJobsData()))
        verify(local).getJobs(recruiterData.id)
        assertNotNull(jobEntities.data)
        assertEquals(jobResponse.size.toLong(), jobEntities.data?.size?.toLong())
    }

    @Test
    fun getJobDetailsTest() {
        val jobDetailsLiveData = MutableLiveData<JobDetailsEntity>()
        `when`(local.getJobDetails(jobResponse[0].id)).thenReturn(jobDetailsLiveData)
        jobRepository.getJobDetails(jobResponse[0].id)

        val jobDetailsEntity = Resource.success(JobDummy.generateJobDetails())
        verify(local).getJobDetails(jobResponse[0].id)
        assertNotNull(jobDetailsEntity.data)
        assertEquals(jobDetailsResponse.id, jobDetailsEntity.data?.id)
        assertEquals(jobDetailsResponse.title, jobDetailsEntity.data?.title)
        assertEquals(jobDetailsResponse.description, jobDetailsEntity.data?.description)
        assertEquals(jobDetailsResponse.address, jobDetailsEntity.data?.address)
        assertEquals(jobDetailsResponse.qualification, jobDetailsEntity.data?.qualification)
        assertEquals(jobDetailsResponse.employment, jobDetailsEntity.data?.employment)
        assertEquals(jobDetailsResponse.type, jobDetailsEntity.data?.type)
        assertEquals(jobDetailsResponse.industry, jobDetailsEntity.data?.industry)
        assertEquals(jobDetailsResponse.salary, jobDetailsEntity.data?.salary)
        assertEquals(jobDetailsResponse.postedBy, jobDetailsEntity.data?.postedBy)
        assertEquals(jobDetailsResponse.postedDate, jobDetailsEntity.data?.postedDate)
        assertEquals(jobDetailsResponse.updatedDate, jobDetailsEntity.data?.updatedDate)
        assertEquals(jobDetailsResponse.closedDate, jobDetailsEntity.data?.closedDate)
        assertEquals(jobDetailsResponse.isOpen, jobDetailsEntity.data?.isOpen)
        assertEquals(
            jobDetailsResponse.isOpenForDisability,
            jobDetailsEntity.data?.isOpenForDisability
        )
        assertEquals(
            jobDetailsResponse.additionalInformation,
            jobDetailsEntity.data?.additionalInformation
        )
    }
}