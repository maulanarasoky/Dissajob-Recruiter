package org.d3ifcool.dissajobrecruiter.ui.applicant

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.databinding.ApplicantItemsBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ApplicantAdapter(private val callback: LoadApplicantDetailsCallback) :
    PagedListAdapter<ApplicantEntity, ApplicantAdapter.ApplicantViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ApplicantEntity>() {
            override fun areItemsTheSame(
                oldItem: ApplicantEntity,
                newItem: ApplicantEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ApplicantEntity,
                newItem: ApplicantEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantViewHolder {
        val itemsApplicantBinding =
            ApplicantItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApplicantViewHolder(itemsApplicantBinding)
    }

    override fun onBindViewHolder(holder: ApplicantViewHolder, position: Int) {
        val job = getItem(position)
        if (job != null) {
            holder.bindItem(job)
        }
    }

    inner class ApplicantViewHolder(private val binding: ApplicantItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: ApplicantEntity) {
            with(binding) {
                val applicantDetails =
                    callback.onLoadApplicantDetailsCallback(items.applicantId.toString())
                tvApplicantName.text = applicantDetails.firstName
                tvAboutMe.text = applicantDetails.aboutMe
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
                try {
                    val time: Long = sdf.parse(items.applyDate).time
                    val now = System.currentTimeMillis()
                    val ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                    tvPostedDate.text = ago
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                tvStatus.text = items.status
            }
        }
    }

    interface LoadApplicantDetailsCallback {
        fun onLoadApplicantDetailsCallback(applicantId: String): ApplicantDetailsEntity
        fun onGetApplicantDetails(applicantDetails: ApplicantDetailsEntity): ApplicantDetailsEntity
    }
}