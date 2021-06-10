package org.d3ifcool.dissajobrecruiter.ui.application

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.databinding.ApplicationItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ApplicationAdapter(
    private val onItemClickCallback: OnApplicationClickCallback,
    private val loadApplicantDataCallback: LoadApplicantDataCallback
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
                loadApplicantDataCallback.onLoadApplicantDetailsCallback(
                    items.applicantId,
                    object :
                        LoadApplicantDataCallback {
                        override fun onLoadApplicantDetailsCallback(
                            applicantId: String,
                            callback: LoadApplicantDataCallback
                        ) {
                            TODO("Not yet implemented")
                        }

                        override fun onGetApplicantDetails(applicantDetails: ApplicantEntity) {
                            tvApplicantName.text = applicantDetails.fullName
                            tvAboutMe.text = applicantDetails.aboutMe
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            sdf.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
                            try {
                                val time: Long = sdf.parse(items.applyDate).time
                                val now = System.currentTimeMillis()
                                val ago =
                                    DateUtils.getRelativeTimeSpanString(
                                        time,
                                        now,
                                        DateUtils.MINUTE_IN_MILLIS
                                    )
                                tvPostedDate.text = ago
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }

                            tvStatus.text = items.status

                            itemView.setOnClickListener {
                                onItemClickCallback.onItemClick(
                                    items.id,
                                    items.jobId,
                                    items.applicantId
                                )
                            }
                        }
                    })
            }
        }
    }

    interface LoadApplicantDataCallback {
        fun onLoadApplicantDetailsCallback(applicantId: String, callback: LoadApplicantDataCallback)
        fun onGetApplicantDetails(applicantDetails: ApplicantEntity)
    }
}