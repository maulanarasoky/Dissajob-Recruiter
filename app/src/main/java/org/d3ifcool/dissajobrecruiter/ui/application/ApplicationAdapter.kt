package org.d3ifcool.dissajobrecruiter.ui.application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.databinding.ApplicationItemBinding
import org.d3ifcool.dissajobrecruiter.ui.application.callback.OnApplicationClickCallback
import org.d3ifcool.dissajobrecruiter.utils.DateUtils

class ApplicationAdapter(
    private val onItemClickCallback: OnApplicationClickCallback,
    private val loadApplicantDataCallback: LoadApplicantDataCallback,
    private val loadJobDataCallback: LoadJobDataCallback
) :
    PagedListAdapter<ApplicationEntity, ApplicationAdapter.ApplicationViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ApplicationEntity>() {
            override fun areItemsTheSame(
                oldItem: ApplicationEntity,
                newItem: ApplicationEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ApplicationEntity,
                newItem: ApplicationEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val itemsApplicationBinding =
            ApplicationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApplicationViewHolder(itemsApplicationBinding)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        val job = getItem(position)
        if (job != null) {
            holder.bindItem(job)
        }
    }

    inner class ApplicationViewHolder(private val binding: ApplicationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: ApplicationEntity) {
            with(binding) {
                tvStatus.text = items.status
                tvPostedDate.text = DateUtils.getPostedDate(items.applyDate.toString())

                loadApplicantData(items.applicantId)
                loadJobData(items.jobId)

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClick(
                        items.id,
                        items.jobId,
                        items.applicantId
                    )
                }
            }
        }

        private fun loadApplicantData(applicantId: String) {
            loadApplicantDataCallback.onLoadApplicantDetailsCallback(
                applicantId,
                object :
                    LoadApplicantDataCallback {
                    override fun onLoadApplicantDetailsCallback(
                        applicantId: String,
                        callback: LoadApplicantDataCallback
                    ) {
                    }

                    override fun onGetApplicantDetails(applicantDetails: ApplicantEntity) {
                        with(binding) {
                            tvApplicantName.text = applicantDetails.fullName


                            if (applicantDetails.imagePath != "-") {
                                val storageRef = Firebase.storage.reference
                                val circularProgressDrawable =
                                    CircularProgressDrawable(itemView.context)
                                circularProgressDrawable.strokeWidth = 5f
                                circularProgressDrawable.centerRadius = 30f
                                circularProgressDrawable.start()
                                Glide.with(itemView.context)
                                    .load(storageRef.child("applicant/profile/images/${applicantDetails.imagePath}"))
                                    .transform(RoundedCorners(20))
                                    .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                                    .error(R.drawable.ic_image_gray_24dp)
                                    .into(imgApplicantPicture)
                            }
                        }
                    }
                })
        }

        private fun loadJobData(jobId: String) {
            loadJobDataCallback.onLoadJobDetailsCallback(jobId, object : LoadJobDataCallback {
                override fun onLoadJobDetailsCallback(
                    jobId: String,
                    callback: LoadJobDataCallback
                ) {
                }

                override fun onGetJobDetails(jobDetails: JobDetailsEntity) {
                    with(binding) {
                        tvApplyAs.text =
                            itemView.resources.getString(R.string.txt_apply_as, jobDetails.title)
                    }
                }
            })
        }
    }

    interface LoadApplicantDataCallback {
        fun onLoadApplicantDetailsCallback(applicantId: String, callback: LoadApplicantDataCallback)
        fun onGetApplicantDetails(applicantDetails: ApplicantEntity)
    }

    interface LoadJobDataCallback {
        fun onLoadJobDetailsCallback(jobId: String, callback: LoadJobDataCallback)
        fun onGetJobDetails(jobDetails: JobDetailsEntity)
    }
}