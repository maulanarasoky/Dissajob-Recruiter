package org.d3ifcool.dissajobrecruiter.ui.job

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.UserEntity
import org.d3ifcool.dissajobrecruiter.databinding.JobItemsBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class JobAdapter : PagedListAdapter<JobEntity, JobAdapter.JobViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JobEntity>() {
            override fun areItemsTheSame(oldItem: JobEntity, newItem: JobEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: JobEntity, newItem: JobEntity): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemsJobBinding =
            JobItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(itemsJobBinding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        if (job != null) {
            holder.bindItem(job)
        }
    }

    inner class JobViewHolder(private val binding: JobItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val storageRef = Firebase.storage.reference
        private val database = FirebaseDatabase.getInstance().reference
        fun bindItem(items: JobEntity) {
            with(binding) {
                tvJobTitle.text = items.title

                getUserData(items.postedBy.toString())

                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
                try {
                    val time: Long = sdf.parse(items.postedDate).time
                    val now = System.currentTimeMillis()
                    val ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                    tvPostedDate.text = ago
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }

        private fun getUserData(userId: String) {
            database.child("users").child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(UserEntity::class.java)
                        if (user != null) {
                            with(binding) {
                                tvRecruiterName.text = itemView.context.resources.getString(
                                    R.string.recruiter_name,
                                    user.firstName,
                                    user.lastName
                                )
                                tvRecruiterAddress.text = user.address

                                if (user.imagePath != "-") {
                                    val circularProgressDrawable =
                                        CircularProgressDrawable(itemView.context)
                                    circularProgressDrawable.strokeWidth = 5f
                                    circularProgressDrawable.centerRadius = 30f
                                    circularProgressDrawable.start()

                                    Glide.with(itemView.context)
                                        .load(storageRef.child("profile/images/${user.imagePath}"))
                                        .placeholder(circularProgressDrawable)
                                        .apply(RequestOptions.overrideOf(500, 500)).centerCrop()
                                        .into(imgRecruiter)
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
    }
}