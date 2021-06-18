package org.d3ifcool.dissajobrecruiter.ui.job

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobrecruiter.utils.dummy.RecruiterDummy
import org.d3ifcool.dissajobrecruiter.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class JobViewModelTest {
    private lateinit var viewModel: JobViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var jobRepository: JobRepository

    @Mock
    private lateinit var jobObserver: Observer<Resource<PagedList<JobEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<JobEntity>

    private val recruiterData = RecruiterDummy.generateRecruiterDetails()

    @Before
    fun setUp() {
        viewModel = JobViewModel(jobRepository)
    }

    @Test
    fun getJobsDataTest() {
        val dummyJobs = Resource.success(pagedList)
        `when`(dummyJobs.data?.size).thenReturn(1)
        val jobs = MutableLiveData<Resource<PagedList<JobEntity>>>()
        jobs.value = dummyJobs

        `when`(jobRepository.getJobs(recruiterData.id)).thenReturn(jobs)
        val jobEntities = viewModel.getJobs(recruiterData.id).value?.data
        verify(jobRepository).getJobs(recruiterData.id)
        assertNotNull(jobEntities)
        assertEquals(1, jobEntities?.size)

        viewModel.getJobs(recruiterData.id).observeForever(jobObserver)
        verify(jobObserver).onChanged(dummyJobs)
    }
}