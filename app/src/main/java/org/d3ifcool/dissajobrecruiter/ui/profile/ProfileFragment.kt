package org.d3ifcool.dissajobrecruiter.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobrecruiter.databinding.FragmentProfileBinding
import org.d3ifcool.dissajobrecruiter.ui.application.ApplicationActivity
import org.d3ifcool.dissajobrecruiter.ui.job.JobActivity
import org.d3ifcool.dissajobrecruiter.ui.settings.SettingsActivity
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.utils.AuthHelper
import org.d3ifcool.dissajobrecruiter.utils.LogoutDialog
import org.d3ifcool.dissajobrecruiter.vo.Status

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var fragmentProfileBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireContext())
            val viewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
            viewModel.getRecruiterData(AuthHelper.currentUser?.uid.toString())
                .observe(viewLifecycleOwner) { profile ->
                    if (profile.data != null) {
                        when (profile.status) {
                            Status.LOADING -> {
                            }
                            Status.SUCCESS -> populateData(profile.data)
                            Status.ERROR -> {
                                Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            //Main menu
            fragmentProfileBinding.profileMainMenu.btnJobMenu.setOnClickListener(this)
            fragmentProfileBinding.profileMainMenu.btnApplicantMenu.setOnClickListener(this)
            fragmentProfileBinding.profileMainMenu.btnSettingsMenu.setOnClickListener(this)

            //SignOut button
            fragmentProfileBinding.btnSignOut.setOnClickListener(this)
        }
    }

    private fun populateData(recruiterProfile: RecruiterEntity) {
        fragmentProfileBinding.tvRecruiterName.text = recruiterProfile.fullName
        fragmentProfileBinding.tvEmail.text = recruiterProfile.email

        if (recruiterProfile.imagePath != "-") {
            val storageRef = Firebase.storage.reference
            val circularProgressDrawable = CircularProgressDrawable(requireContext())
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(requireContext())
                .load(storageRef.child("recruiter/profile/images/${recruiterProfile.imagePath}"))
                .transform(RoundedCorners(20))
                .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                .error(R.drawable.ic_image_gray_24dp)
                .into(fragmentProfileBinding.imgProfile)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnApplicantMenu -> {
                activity?.startActivity(Intent(activity, ApplicationActivity::class.java))
            }
            R.id.btnJobMenu -> {
                activity?.startActivity(Intent(activity, JobActivity::class.java))
            }
            R.id.btnSettingsMenu -> {
                activity?.startActivity(Intent(activity, SettingsActivity::class.java))
            }
            R.id.btnSignOut -> {
                LogoutDialog().show(parentFragmentManager, LogoutDialog::class.java.simpleName)
            }
        }
    }
}