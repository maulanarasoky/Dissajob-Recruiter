package org.d3ifcool.dissajobrecruiter.data.source.repository.application

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import com.nhaarman.mockitokotlin2.verify
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalApplicationSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteApplicationSource
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.PagedListUtil
import org.d3ifcool.dissajobrecruiter.utils.dummy.ApplicationDummy
import org.d3ifcool.dissajobrecruiter.utils.dummy.RecruiterDummy
import org.d3ifcool.dissajobrecruiter.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ApplicationRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteApplicationSource::class.java)
    private val local = mock(LocalApplicationSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val applicationRepository = FakeApplicationRepository(remote, local, appExecutors)

    private val applicationResponse = ApplicationDummy.generateApplicationsData()
    private val recruiterData = RecruiterDummy.generateRecruiterDetails()

    @Test
    fun getApplications() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ApplicationEntity>
        `when`(local.getApplications(recruiterData.id)).thenReturn(dataSourceFactory)
        applicationRepository.getApplications(recruiterData.id)

        val applicationEntities =
            Resource.success(PagedListUtil.mockPagedList(ApplicationDummy.generateApplicationsData()))
        verify(local).getApplications(recruiterData.id)
        assertNotNull(applicationEntities.data)
        assertEquals(applicationResponse.size.toLong(), applicationEntities.data?.size?.toLong())
    }

    @Test
    fun getAcceptedApplications() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ApplicationEntity>
        `when`(local.getAcceptedApplications(recruiterData.id)).thenReturn(dataSourceFactory)
        applicationRepository.getAcceptedApplications(recruiterData.id)

        val applicationEntities =
            Resource.success(PagedListUtil.mockPagedList(ApplicationDummy.generateApplicationsData()))
        verify(local).getAcceptedApplications(recruiterData.id)
        assertNotNull(applicationEntities.data)
        assertEquals(applicationResponse.size.toLong(), applicationEntities.data?.size?.toLong())
    }
}