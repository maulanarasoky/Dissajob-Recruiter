package org.d3ifcool.dissajobrecruiter.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityHomeBinding
import org.d3ifcool.dissajobrecruiter.ui.auth.AuthViewModel
import org.d3ifcool.dissajobrecruiter.ui.job.JobFragment
import org.d3ifcool.dissajobrecruiter.ui.notification.NotificationFragment
import org.d3ifcool.dissajobrecruiter.ui.profile.ProfileFragment
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInActivity

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val STATE_RESULT = "state_result"
    }

    private lateinit var activityHomeBinding: ActivityHomeBinding

    private var bottomNavState = 0

    private lateinit var mUserObserver: Observer<FirebaseUser?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            this.finish()
            return
        }

        val viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        mUserObserver = Observer { updateUI(savedInstanceState) }
        viewModel.authState.observe(this, mUserObserver)
    }

    private fun updateUI(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            bottomNavState = savedInstanceState.getInt(STATE_RESULT, 0)
        }

        activityHomeBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            if (bottomNavState != item.itemId) {
                bottomNavState = item.itemId
                when (item.itemId) {
                    R.id.home -> {
                        loadHomeFragment()
                    }
                    R.id.job -> {
                        loadJobFragment()
                    }
                    R.id.notification -> {
                        loadNotificationFragment()
                    }
                    R.id.profile -> {
                        loadProfileFragment()
                    }
                }
            }
            true
        }
        if (savedInstanceState == null || bottomNavState == 0) {
            activityHomeBinding.bottomNavigation.selectedItemId = R.id.home
        }
    }

    private fun loadHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_layout, HomeFragment(), HomeFragment::class.java.simpleName)
            .commit()
    }

    private fun loadJobFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container_layout,
                JobFragment(),
                JobFragment::class.java.simpleName
            )
            .commit()
    }

    private fun loadNotificationFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container_layout,
                NotificationFragment(),
                NotificationFragment::class.java.simpleName
            )
            .commit()
    }

    private fun loadProfileFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container_layout,
                ProfileFragment(),
                ProfileFragment::class.java.simpleName
            )
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_RESULT, bottomNavState)
    }
}