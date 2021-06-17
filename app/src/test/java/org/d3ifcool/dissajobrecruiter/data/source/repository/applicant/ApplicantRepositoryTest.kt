package org.d3ifcool.dissajobrecruiter.data.source.repository.applicant

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.verify
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalApplicantSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteApplicantSource
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobrecruiter.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ApplicantRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteApplicantSource::class.java)
    private val local = mock(LocalApplicantSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val applicantRepository = FakeApplicantRepository(remote, local, appExecutors)

    private val applicantData = ApplicantDummy.generateApplicantData()

    @Test
    fun getApplicantData() {
        val dataSourceFactory = MutableLiveData<ApplicantEntity>()
        `when`(local.getApplicantDetails(applicantData.id)).thenReturn(dataSourceFactory)
        applicantRepository.getApplicantDetails(applicantData.id)

        val applicantEntity = Resource.success(ApplicantDummy.generateApplicantData())
        verify(local).getApplicantDetails(applicantData.id)
        assertNotNull(applicantEntity.data)
        assertEquals(applicantData.id, applicantEntity.data?.id)
        assertEquals(applicantData.firstName, applicantEntity.data?.firstName)
        assertEquals(applicantData.lastName, applicantEntity.data?.lastName)
        assertEquals(applicantData.fullName, applicantEntity.data?.fullName)
        assertEquals(applicantData.email, applicantEntity.data?.email)
        assertEquals(applicantData.aboutMe, applicantEntity.data?.aboutMe)
        assertEquals(applicantData.phoneNumber, applicantEntity.data?.phoneNumber)
        assertEquals(applicantData.imagePath, applicantEntity.data?.imagePath)
    }
}