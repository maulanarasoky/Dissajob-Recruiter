package org.d3ifcool.dissajobrecruiter.ui.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.RecruiterResponseEntity
import org.d3ifcool.dissajobrecruiter.databinding.ActivityChangeProfileBinding
import org.d3ifcool.dissajobrecruiter.ui.profile.RecruiterViewModel
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.UploadProfilePictureCallback
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.AuthHelper
import org.d3ifcool.dissajobrecruiter.vo.Status

class ChangeProfileActivity : AppCompatActivity(), View.OnClickListener, UpdateProfileCallback,
    UploadProfilePictureCallback {

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000

        //Permission code
        private const val PERMISSION_CODE = 1001
    }

    private lateinit var activityChangeProfileBinding: ActivityChangeProfileBinding

    private lateinit var viewModel: RecruiterViewModel

    private lateinit var dialog: SweetAlertDialog

    private lateinit var image: Uri

    private var isUpdateImage = false

    private lateinit var recruiterData: RecruiterEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangeProfileBinding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(activityChangeProfileBinding.root)

        activityChangeProfileBinding.toolbar.title =
            resources.getString(R.string.change_profile_title)
        setSupportActionBar(activityChangeProfileBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]

        showCurrentProfileData()

        activityChangeProfileBinding.imgProfilePic.setOnClickListener(this)
        activityChangeProfileBinding.btnUpdate.setOnClickListener(this)
    }

    private fun showCurrentProfileData() {
        viewModel.getRecruiterData(AuthHelper.currentUser?.uid.toString())
            .observe(this) { profileData ->
                if (profileData.data != null) {
                    when (profileData.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            recruiterData = profileData.data
                            if (profileData.data.imagePath != "-") {
                                val storageRef = Firebase.storage.reference
                                val circularProgressDrawable = CircularProgressDrawable(this)
                                circularProgressDrawable.strokeWidth = 5f
                                circularProgressDrawable.centerRadius = 30f
                                circularProgressDrawable.start()

                                Glide.with(this)
                                    .load(storageRef.child("recruiter/profile/images/${profileData.data.imagePath}"))
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

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //permission already granted
                pickImageFromGallery()
            }
        } else {
            //system OS is < Marshmallow
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data?.data != null) {
                image = data.data!!
                activityChangeProfileBinding.imgProfilePic.setImageURI(data.data)
                isUpdateImage = true
            }
        }
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityChangeProfileBinding.etFirstName.text.toString().trim())) {
            activityChangeProfileBinding.etFirstName.error =
                getString(R.string.txt_error_form_text, "Nama depan")
            return
        }
        if (TextUtils.isEmpty(activityChangeProfileBinding.etLastName.text.toString().trim())) {
            activityChangeProfileBinding.etLastName.error =
                getString(R.string.txt_error_form_text, "Nama belakang")
            return
        }
        if (TextUtils.isEmpty(
                activityChangeProfileBinding.etAddress.text.toString().trim()
            ) || activityChangeProfileBinding.etAddress.text.toString() == "-"
        ) {
            activityChangeProfileBinding.etAddress.error =
                getString(R.string.txt_error_form_text, "Alamat")
            return
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.loading)
        dialog.setCancelable(false)
        dialog.show()

        if (isUpdateImage) {
            viewModel.uploadRecruiterProfilePicture(image, this)
        } else {
            updateUserProfile("-")
        }
    }

    private fun updateUserProfile(imageId: String) {
        val firstName = activityChangeProfileBinding.etFirstName.text.toString().trim()
        val lastName = activityChangeProfileBinding.etLastName.text.toString().trim()
        val fullName = "$firstName $lastName"
        val email = recruiterData.email.toString()
        val address = activityChangeProfileBinding.etAddress.text.toString().trim()
        val phoneNumber = recruiterData.phoneNumber.toString()
        val profileData = RecruiterResponseEntity(
            AuthHelper.currentUser?.uid.toString(),
            firstName,
            lastName,
            fullName,
            email,
            address,
            phoneNumber,
            imageId
        )

        viewModel.updateRecruiterData(profileData, this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgProfilePic -> checkPermission()
            R.id.btnUpdate -> {
                formValidation()
            }
        }
    }

    override fun onSuccess() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_success_update_profile, "Profil")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailure(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onSuccessUpload(imageId: String) {
        updateUserProfile(imageId)
    }

    override fun onFailureUpload(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId)
        dialog.setCancelable(false)
        dialog.show()
    }
}