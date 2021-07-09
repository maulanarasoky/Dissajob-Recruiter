package org.d3ifcool.dissajobrecruiter.ui.notification

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
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.notification.NotificationEntity
import org.d3ifcool.dissajobrecruiter.databinding.NotificationItemBinding
import org.d3ifcool.dissajobrecruiter.ui.applicant.LoadApplicantDataCallback
import org.d3ifcool.dissajobrecruiter.utils.DateUtils

class NotificationAdapter(
    private val onItemClickCallback: OnNotificationClickCallback,
    private val loadApplicantDataCallback: LoadApplicantDataCallback
) :
    PagedListAdapter<NotificationEntity, NotificationAdapter.NotificationViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NotificationEntity>() {
            override fun areItemsTheSame(
                oldItem: NotificationEntity,
                newItem: NotificationEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotificationEntity,
                newItem: NotificationEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemsNotificationBinding =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(itemsNotificationBinding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        if (notification != null) {
            holder.bindItem(notification)
        }
    }

    inner class NotificationViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: NotificationEntity) {
            with(binding) {
                loadApplicantData(items.applicantId)
                tvNotificationDate.text = DateUtils.getPostedDate(items.notificationDate.toString())
                btnViewApplication.setOnClickListener {
                    onItemClickCallback.onItemClick(
                        items.applicationId,
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
                            tvNotificationTitle.text = itemView.resources.getString(
                                R.string.txt_notification_title,
                                applicantDetails.fullName
                            )

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
    }
}