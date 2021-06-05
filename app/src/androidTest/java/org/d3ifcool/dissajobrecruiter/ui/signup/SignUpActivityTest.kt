package org.d3ifcool.dissajobrecruiter.ui.signup

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInActivity
import org.d3ifcool.dissajobrecruiter.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.regex.Pattern

class SignUpActivityTest {
    private val dummyFirstName = "Maulana"
    private val dummyLastName = "Rasoky Nasution"
    private val dummyRole = "HRD"
    private val dummyEmail = "example@example.com"
    private val dummyInvalidEmail = "example@@"
    private val dummyPassword = "123456"
    private val dummyConfirmPassword = "123456"
    private val dummyWrongConfirmPassword = "654321"

    @Before
    fun setUp() {
        ActivityScenario.launch(SignUpActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @Test
    fun showFirstNameEditTextErrorTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.btnSignUp)).perform(click())

        onView(withId(R.id.etFirstName)).check(matches(hasErrorText("Nama depan tidak boleh kosong")))
    }

    @Test
    fun showRoleEditTextErrorTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etFirstName)).perform(typeText(dummyFirstName))
        onView(withId(R.id.etLastName)).perform(typeText(dummyLastName))

        onView(withId(R.id.btnSignUp)).perform(click())

        onView(withId(R.id.etRole)).check(matches(hasErrorText("Role tidak boleh kosong")))
    }

    @Test
    fun showEmailEditTextErrorTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etFirstName)).perform(typeText(dummyFirstName))
        onView(withId(R.id.etLastName)).perform(typeText(dummyLastName))
        onView(withId(R.id.etRole)).perform(typeText(dummyRole))

        onView(withId(R.id.btnSignUp)).perform(click())

        onView(withId(R.id.etEmail)).check(matches(hasErrorText("Email tidak boleh kosong")))
    }

    @Test
    fun showPasswordEditTextErrorTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etFirstName)).perform(typeText(dummyFirstName))
        onView(withId(R.id.etLastName)).perform(typeText(dummyLastName))
        onView(withId(R.id.etRole)).perform(typeText(dummyRole))
        onView(withId(R.id.etEmail)).perform(typeText(dummyEmail))

        onView(withId(R.id.btnSignUp)).perform(click())

        onView(withId(R.id.etPassword)).check(matches(hasErrorText("Password tidak boleh kosong")))
    }

    @Test
    fun showConfirmPasswordEditTextErrorTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etFirstName)).perform(typeText(dummyFirstName))
        onView(withId(R.id.etLastName)).perform(typeText(dummyLastName))
        onView(withId(R.id.etEmail)).perform(typeText(dummyEmail))
        onView(withId(R.id.etRole)).perform(typeText(dummyRole))
        onView(withId(R.id.etPassword)).perform(typeText(dummyPassword))

        onView(withId(R.id.btnSignUp)).perform(click())

        onView(withId(R.id.etConfirmPassword)).check(matches(hasErrorText("Confirm password tidak boleh kosong")))
    }

    @Test
    fun showPasswordNotMatchEditTextErrorTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etFirstName)).perform(typeText(dummyFirstName))
        onView(withId(R.id.etLastName)).perform(typeText(dummyLastName))
        onView(withId(R.id.etEmail)).perform(typeText(dummyEmail))
        onView(withId(R.id.etRole)).perform(typeText(dummyRole))
        onView(withId(R.id.etPassword)).perform(typeText(dummyPassword))
        onView(withId(R.id.etConfirmPassword)).perform(typeText(dummyWrongConfirmPassword))

        onView(withId(R.id.btnSignUp)).perform(click())

        if (dummyPassword != dummyWrongConfirmPassword) {
            onView(withId(R.id.etPassword)).check(matches(hasErrorText("Password dan confirm password tidak cocok")))
            onView(withId(R.id.etConfirmPassword)).check(matches(hasErrorText("Password dan confirm password tidak cocok")))
        }
    }

    @Test
    fun showIsInvalidEmailEditTextErrorTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etFirstName)).perform(typeText(dummyFirstName))
        onView(withId(R.id.etLastName)).perform(typeText(dummyLastName))
        onView(withId(R.id.etEmail)).perform(typeText(dummyInvalidEmail))
        onView(withId(R.id.etRole)).perform(typeText(dummyRole))
        onView(withId(R.id.etPassword)).perform(typeText(dummyPassword))
        onView(withId(R.id.etConfirmPassword)).perform(typeText(dummyConfirmPassword))

        if (!isValidMail()) {
            onView(withId(R.id.etEmail)).check(matches(hasErrorText("Email tidak valid")))
        }
    }

    @Test
    fun clickSignUpButtonTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etFirstName)).perform(typeText(dummyFirstName))
        onView(withId(R.id.etLastName)).perform(typeText(dummyLastName))
        onView(withId(R.id.etEmail)).perform(typeText(dummyEmail))
        onView(withId(R.id.etRole)).perform(typeText(dummyRole))
        onView(withId(R.id.etPassword)).perform(typeText(dummyPassword))
        onView(withId(R.id.etConfirmPassword)).perform(typeText(dummyConfirmPassword))

        onView(withId(R.id.btnSignUp)).perform(click())

        onView(withText("Silahkan lakukan verifikasi email")).check(matches(isDisplayed()))
    }

    @Test
    fun clickSignInButtonTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.btnSignIn)).perform(click())

        Intents.init()
        Intents.intended(IntentMatchers.hasComponent(SignInActivity::class.simpleName))
    }

    private fun isValidMail(): Boolean {
        val emailString = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        return Pattern.compile(emailString).matcher(dummyInvalidEmail).matches()
    }
}