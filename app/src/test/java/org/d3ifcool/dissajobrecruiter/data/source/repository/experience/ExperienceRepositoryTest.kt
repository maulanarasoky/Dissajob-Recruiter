package org.d3ifcool.dissajobrecruiter.data.source.repository.experience

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalEducationSource
import org.d3ifcool.dissajobrecruiter.data.source.local.source.LocalExperienceSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteEducationSource
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteExperienceSource
import org.d3ifcool.dissajobrecruiter.data.source.repository.education.FakeEducationRepository
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobrecruiter.utils.dummy.EducationDummy
import org.junit.Rule
import org.mockito.Mockito

class ExperienceRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = Mockito.mock(RemoteExperienceSource::class.java)
    private val local = Mockito.mock(LocalExperienceSource::class.java)
    private val appExecutors = Mockito.mock(AppExecutors::class.java)

    private val experienceRepository = FakeExperienceRepository(remote, local, appExecutors)
    private val experienceResponse = EducationDummy.generateEducationData()
    private val applicantData = ApplicantDummy.generateApplicantData()
}