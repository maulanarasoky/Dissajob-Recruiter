package org.d3ifcool.dissajobrecruiter.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityChangePhoneNumberBinding
import org.d3ifcool.dissajobrecruiter.databinding.ActivityChangeProfileBinding
import org.d3ifcool.dissajobrecruiter.ui.profile.ProfileViewModel
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.AuthHelper
import org.d3ifcool.dissajobrecruiter.vo.Status

class ChangeProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityChangeProfileBinding: ActivityChangeProfileBinding

    private lateinit var viewModel: ProfileViewModel

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangeProfileBinding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(activityChangeProfileBinding.root)

        activityChangeProfileBinding.activityHeader.tvHeaderTitle.text =
            resources.getString(R.string.change_email_title)

        showCurrentProfileData()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        activityChangeProfileBinding.activityHeader.imgBackBtn.setOnClickListener(this)
        activityChangeProfileBinding.btnUpdate.setOnClickListener(this)
    }

    private fun showCurrentProfileData() {
        viewModel.getUserProfile(AuthHelper.currentUser?.uid.toString())
            .observe(this) { profileData ->
                if (profileData.data != null) {
                    when (profileData.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            if (profileData.data.imagePath != "-") {
                                val storageRef = Firebase.storage.reference
                                val circularProgressDrawable = CircularProgressDrawable(this)
                                circularProgressDrawable.strokeWidth = 5f
                                circularProgressDrawable.centerRadius = 30f
                                circularProgressDrawable.start()

                                Glide.with(this)
                                    .load(storageRef.child("profile/images/${profileData.data.imagePath}"))
                                    .placeholder(circularProgressDrawable)
                                    .apply(RequestOptions.overrideOf(500, 500)).centerCrop()
                                    .into(activityChangeProfileBinding.imgProfilePic)
                            }

                            activityChangeProfileBinding.etFirstName.setText(profileData.data.firstName)
                            activityChangeProfileBinding.etLastName.setText(profileData.data.lastName)
                            activityChangeProfileBinding.etAddress.setText(profileData.data.address)
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBackBtn -> finish()
            R.id.btnUpdate -> {

            }
        }
    }
}