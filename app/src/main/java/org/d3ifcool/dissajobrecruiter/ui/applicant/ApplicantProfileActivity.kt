package org.d3ifcool.dissajobrecruiter.ui.applicant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobrecruiter.databinding.ActivityApplicantProfileBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.experience.ExperienceAdapter
import org.d3ifcool.dissajobrecruiter.ui.applicant.experience.ExperienceViewModel
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class ApplicantProfileActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val APPLICANT_ID = "applicant_id"
    }

    private lateinit var activityApplicantProfileBinding: ActivityApplicantProfileBinding

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var experienceViewModel: ExperienceViewModel

    private lateinit var educationViewModel: EducationViewModel

    private lateinit var experienceAdapter: ExperienceAdapter

    private lateinit var educationAdapter: EducationAdapter

    private var isAboutMeExpanded = false

    private lateinit var applicantId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApplicantProfileBinding = ActivityApplicantProfileBinding.inflate(layoutInflater)
        setContentView(activityApplicantProfileBinding.root)

        if (intent == null) {
            finish()
            return
        }

        applicantId = intent.getStringExtra(APPLICANT_ID).toString()
        val factory = ViewModelFactory.getInstance(this)
        experienceViewModel = ViewModelProvider(this, factory)[ExperienceViewModel::class.java]
        educationViewModel = ViewModelProvider(this, factory)[EducationViewModel::class.java]
        applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
        applicantViewModel.getApplicantDetails(applicantId)
            .observe(this) { applicantProfile ->
                if (applicantProfile.data != null) {
                    when (applicantProfile.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> populateApplicantData(applicantProfile.data)
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        experienceAdapter = ExperienceAdapter()
        experienceViewModel.getApplicantExperiences(applicantId).observe(this) { experiences ->
                if (experiences != null) {
                    when (experiences.status) {
                        Status.LOADING -> activityApplicantProfileBinding.workExperienceSection.progressBar.visibility =
                            View.VISIBLE
                        Status.SUCCESS -> {
                            activityApplicantProfileBinding.workExperienceSection.progressBar.visibility =
                                View.GONE
                            if (experiences.data?.isNotEmpty() == true) {
                                experienceAdapter.submitList(experiences.data)
                                experienceAdapter.notifyDataSetChanged()
                            } else {
                                activityApplicantProfileBinding.workExperienceSection.tvNoData.visibility =
                                    View.VISIBLE
                            }
                        }
                        Status.ERROR -> {
                            activityApplicantProfileBinding.workExperienceSection.progressBar.visibility =
                                View.GONE
                            activityApplicantProfileBinding.workExperienceSection.tvNoData.visibility =
                                View.VISIBLE
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        with(activityApplicantProfileBinding.workExperienceSection.rvWorkExperience) {
            layoutManager = LinearLayoutManager(this@ApplicantProfileActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@ApplicantProfileActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = experienceAdapter
        }

        educationAdapter = EducationAdapter()
        educationViewModel.getApplicantEducations(applicantId)
            .observe(this) { educations ->
                if (educations != null) {
                    when (educations.status) {
                        Status.LOADING -> activityApplicantProfileBinding.educationalBackgroundSection.progressBar.visibility =
                            View.VISIBLE
                        Status.SUCCESS -> {
                            activityApplicantProfileBinding.educationalBackgroundSection.progressBar.visibility =
                                View.GONE
                            if (educations.data?.isNotEmpty() == true) {
                                educationAdapter.submitList(educations.data)
                                educationAdapter.notifyDataSetChanged()
                            } else {
                                activityApplicantProfileBinding.educationalBackgroundSection.tvNoData.visibility =
                                    View.VISIBLE
                            }
                        }
                        Status.ERROR -> {
                            activityApplicantProfileBinding.educationalBackgroundSection.progressBar.visibility =
                                View.GONE
                            activityApplicantProfileBinding.educationalBackgroundSection.tvNoData.visibility =
                                View.VISIBLE
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        with(activityApplicantProfileBinding.educationalBackgroundSection.rvEducationalBackground) {
            layoutManager = LinearLayoutManager(this@ApplicantProfileActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@ApplicantProfileActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = educationAdapter
        }

        //Upload media section
        activityApplicantProfileBinding.uploadMediaSection.root.setOnClickListener(this)
    }

    private fun populateApplicantData(applicantProfile: ApplicantEntity) {
        activityApplicantProfileBinding.profileSection.tvApplicantName.text =
            applicantProfile.fullName.toString()
        activityApplicantProfileBinding.profileSection.tvEmail.text = applicantProfile.email.toString()
        activityApplicantProfileBinding.profileSection.tvPhoneNumber.text =
            applicantProfile.phoneNumber.toString()
        activityApplicantProfileBinding.aboutMeSection.tvAboutMe.text = applicantProfile.aboutMe.toString()
        activityApplicantProfileBinding.aboutMeSection.tvAboutMe.setOnClickListener(tvAboutMeClickListener)

        if (applicantProfile.imagePath != "-") {
            val storageRef = Firebase.storage.reference
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(this)
                .load(storageRef.child("applicant/profile/images/${applicantProfile.imagePath}"))
                .transform(RoundedCorners(20))
                .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                .error(R.drawable.ic_profile_gray_24dp)
                .into(activityApplicantProfileBinding.profileSection.imgApplicantPicture)
        }
    }

    private val tvAboutMeClickListener = View.OnClickListener {
        isAboutMeExpanded = if (isAboutMeExpanded) {
            //This will shrink textview to 2 lines if it is expanded.
            activityApplicantProfileBinding.aboutMeSection.tvAboutMe.maxLines = 3
            false
        } else {
            //This will expand the textview if it is of 2 lines
            activityApplicantProfileBinding.aboutMeSection.tvAboutMe.maxLines = Integer.MAX_VALUE
            true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.uploadMediaSection -> startActivity(Intent(this, MediaActivity::class.java))
        }
    }
}