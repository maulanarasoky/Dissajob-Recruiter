package org.d3ifcool.dissajobrecruiter.ui.job

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.entity.JobEntity
import org.d3ifcool.dissajobrecruiter.databinding.JobItemsBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class JobAdapter : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    private var listData = ArrayList<JobEntity>()

    fun setData(data: List<JobEntity>) {
        if (data.isEmpty()) return
        this.listData.clear()
        this.listData.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemsJobBinding =
            JobItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(itemsJobBinding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bindItem(listData[position])
    }

    inner class JobViewHolder(private val binding: JobItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: JobEntity) {
            with(binding) {
                tvJobTitle.text = items.title.toString()
                tvDescription.text = items.description.toString()

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

                if (items.isOpen == true) {
                    tvStatus.setBackgroundResource(R.drawable.bg_open_status)
                    tvStatus.text = itemView.resources.getText(R.string.open_status)
                } else {
                    tvStatus.setBackgroundResource(R.drawable.bg_closed_status)
                    tvStatus.text = itemView.resources.getText(R.string.closed_status)
                }
            }
        }
    }

    override fun getItemCount(): Int = listData.size
}