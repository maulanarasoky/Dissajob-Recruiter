package org.d3ifcool.dissajobrecruiter.ui.job

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.ui.home.HomeActivity
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.dummy.JobDummy
import org.junit.After
import org.junit.Before
import org.junit.Test

class JobDetailsActivityTest {
    private val dummyDetailsJob = JobDummy.generateJobDetails()
    private val applicantsTotal = 0

    @Before
    fun setUp() {
        ActivityScenario.launch(HomeActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @Test
    fun loadJobDetailsTest() {
        onView(withId(R.id.job)).check(matches(isDisplayed()))
        onView(withId(R.id.job)).perform(click())

        onView(withId(R.id.rvJob)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                3,
                click()
            )
        )

        onView(withId(R.id.tvJobTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobTitle)).check(matches(withText(dummyDetailsJob.title)))

        onView(withId(R.id.tvJobAddress)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobAddress)).check(matches(withText(dummyDetailsJob.address)))

        onView(isRoot()).perform(swipeUp())

        onView(withId(R.id.tvJobType)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobType)).check(matches(withText("${dummyDetailsJob.type} \u2022 ${dummyDetailsJob.industry}")))

        onView(withId(R.id.tvJobPostedDateAndApplicants)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobPostedDateAndApplicants)).check(matches(withText("${dummyDetailsJob.postedDate} \u2022 $applicantsTotal pelamar")))

        onView(withId(R.id.tvDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.tvDescription)).check(matches(withText(dummyDetailsJob.description)))

        if (dummyDetailsJob.isOpenForDisability) {
            onView(withId(R.id.tvAdditionalInformation)).check(matches(isDisplayed()))
            onView(withId(R.id.tvAdditionalInformation)).check(matches(withText("Keterangan tambahan : ${dummyDetailsJob.additionalInformation}")))
        }

        onView(withId(R.id.tvQualification)).check(matches(isDisplayed()))
        onView(withId(R.id.tvQualification)).check(matches(withText(dummyDetailsJob.qualification)))

        onView(withId(R.id.tvIndustry)).check(matches(isDisplayed()))
        onView(withId(R.id.tvIndustry)).check(matches(withText(dummyDetailsJob.industry)))

        onView(withId(R.id.tvJobSalary)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobSalary)).check(matches(withText(dummyDetailsJob.salary)))
    }
}