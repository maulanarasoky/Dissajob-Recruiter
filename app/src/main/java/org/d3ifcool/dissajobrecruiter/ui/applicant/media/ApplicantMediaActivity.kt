package org.d3ifcool.dissajobrecruiter.ui.applicant.media

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.rajat.pdfviewer.PdfViewerActivity
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityApplicantMediaBinding
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status

class ApplicantMediaActivity : AppCompatActivity(), LoadPdfCallback, OnMediaItemClickListener {

    companion object {
        const val APPLICANT_ID = "applicant_id"
    }

    private lateinit var activityApplicantMediaBinding: ActivityApplicantMediaBinding

    private lateinit var mediaViewModel: MediaViewModel

    private lateinit var mediaAdapter: MediaAdapter

    private lateinit var applicantId: String

    private val storageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApplicantMediaBinding = ActivityApplicantMediaBinding.inflate(layoutInflater)
        setContentView(activityApplicantMediaBinding.root)

        activityApplicantMediaBinding.toolbar.title = resources.getString(R.string.txt_media)
        setSupportActionBar(activityApplicantMediaBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent == null) {
            finish()
            return
        }

        applicantId = intent.getStringExtra(APPLICANT_ID).toString()
        val factory = ViewModelFactory.getInstance(this)
        mediaViewModel = ViewModelProvider(this, factory)[MediaViewModel::class.java]
        mediaAdapter = MediaAdapter(this, this)
        mediaViewModel.getApplicantMedia(applicantId)
            .observe(this) { mediaFiles ->
                if (mediaFiles != null) {
                    when (mediaFiles.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (mediaFiles.data?.isNotEmpty() == true) {
                                mediaAdapter.submitList(mediaFiles.data)
                                mediaAdapter.notifyDataSetChanged()
                                activityApplicantMediaBinding.tvNoData.visibility = View.GONE
                            } else {
                                activityApplicantMediaBinding.tvNoData.visibility = View.VISIBLE
                            }
                        }
                        Status.ERROR -> {
                            showLoading(false)
                            showErrorToast()
                        }
                    }
                }
            }
        with(activityApplicantMediaBinding.rvMedia) {
            recycledViewPool.setMaxRecycledViews(0, 0)
            layoutManager = LinearLayoutManager(this@ApplicantMediaActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@ApplicantMediaActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = mediaAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activityApplicantMediaBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityApplicantMediaBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showErrorToast() {
        Toast.makeText(
            this,
            resources.getString(R.string.txt_error_occurred),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onLoadPdfData(mediaId: String, callback: LoadPdfCallback) {
        mediaViewModel.getMediaById(mediaId).observe(this) { file ->
            if (file != null) {
                callback.onPdfDataReceived(file)
            } else {
                showErrorToast()
            }
        }
    }

    override fun onPdfDataReceived(mediaFile: ByteArray) {
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

    override fun onItemClick(fileId: String, mediaName: String) {
        storageRef.child("applicant/media/$fileId").downloadUrl.addOnSuccessListener {
            startActivity(
                PdfViewerActivity.launchPdfFromUrl(
                    this,
                    it.toString(),
                    mediaName,
                    "/dissajob/media",
                    enableDownload = true
                )
            )
        }.addOnFailureListener {
            showErrorToast()
        }
    }
}