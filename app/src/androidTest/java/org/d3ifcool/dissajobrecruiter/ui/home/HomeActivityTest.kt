package org.d3ifcool.dissajobrecruiter.ui.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.d3ifcool.dissajobrecruiter.utils.dummy.RecruiterDummy
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeActivityTest {
    private val dummyRecruiterData = RecruiterDummy.generateRecruiterDetails()

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
    fun loadHomeFragmentTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.rvApplicant))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rvApplicant)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        )
    }

    @Test
    fun loadProfileFragmentTest() {
        onView(withId(R.id.profile)).check(matches(isDisplayed()))
        onView(withId(R.id.profile)).perform(click())

        onView(withId(R.id.imgProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.tvRole)).check(matches(isDisplayed()))
        onView(withId(R.id.tvRole)).check(matches(withText(dummyRecruiterData.role)))

        onView(withId(R.id.tvRecruiterName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvRecruiterName)).check(matches(withText(dummyRecruiterData.fullName)))

        onView(withId(R.id.tvEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.tvEmail)).check(matches(withText(dummyRecruiterData.email)))

        onView(withId(R.id.profileMainMenu)).check(matches(isDisplayed()))
    }
}